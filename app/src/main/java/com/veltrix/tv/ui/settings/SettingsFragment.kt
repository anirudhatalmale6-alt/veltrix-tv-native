package com.veltrix.tv.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.veltrix.tv.R
import com.veltrix.tv.data.CustomerPrefsManager
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.ui.login.LoginActivity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.welcome.WelcomeActivity
import com.veltrix.tv.util.DashboardTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private lateinit var tvServer: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvExpiry: TextView
    private lateinit var tvMaxConn: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnDeleteAccount: Button

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvServer = view.findViewById(R.id.tvServer)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvStatus = view.findViewById(R.id.tvStatus)
        tvExpiry = view.findViewById(R.id.tvExpiry)
        tvMaxConn = view.findViewById(R.id.tvMaxConn)
        btnLogout = view.findViewById(R.id.btnLogout)

        val prefs = MainActivity.prefsInstance
        tvServer.text = prefs.getBaseUrl()
        tvUsername.text = prefs.username

        loadAccountInfo()

        btnLogout.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount)
        btnDeleteAccount.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
        btnDeleteAccount.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun loadAccountInfo() {
        val prefs = MainActivity.prefsInstance

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    MainActivity.apiService.authenticate(prefs.username, prefs.password)
                }

                val userInfo = response.userInfo
                tvStatus.text = userInfo?.status ?: "Unknown"

                val expDate = userInfo?.expDate
                if (!expDate.isNullOrEmpty()) {
                    try {
                        val timestamp = expDate.toLongOrNull()
                        if (timestamp != null) {
                            val date = Date(timestamp * 1000)
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            tvExpiry.text = sdf.format(date)
                        } else {
                            tvExpiry.text = expDate
                        }
                    } catch (e: Exception) {
                        tvExpiry.text = expDate
                    }
                } else {
                    tvExpiry.text = "N/A"
                }

                tvMaxConn.text = userInfo?.maxConnections ?: "N/A"

            } catch (e: Exception) {
                tvStatus.text = "Unable to fetch"
                tvExpiry.text = "-"
                tvMaxConn.text = "-"
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirm))
            .setPositiveButton(R.string.yes) { _, _ ->
                performLogout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun performLogout() {
        val prefs = PrefsManager.getInstance(requireContext())
        prefs.clear()

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getInstance(requireContext()).favoriteDao().deleteAll()
            }
        }

        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("This will permanently delete your account, cancel your subscription, and log you out. This cannot be undone.\n\nAre you sure?")
            .setPositiveButton("Delete") { _, _ ->
                performDeleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performDeleteAccount() {
        val customerPrefs = CustomerPrefsManager.getInstance(requireContext())
        val email = customerPrefs.customerEmail

        if (email.isEmpty()) {
            performLogout()
            return
        }

        btnDeleteAccount.isEnabled = false
        btnDeleteAccount.text = "Deleting..."

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val deleted = withContext(Dispatchers.IO) {
                    val json = JSONObject().apply { put("email", email) }
                    val body = json.toString().toRequestBody("application/json".toMediaType())
                    val request = Request.Builder()
                        .url("${DashboardTracker.BASE_URL}/api/customer/delete")
                        .header("X-API-Key", DashboardTracker.API_KEY)
                        .post(body)
                        .build()
                    httpClient.newCall(request).execute().isSuccessful
                }

                customerPrefs.clear()
                val prefs = PrefsManager.getInstance(requireContext())
                prefs.clear()
                DashboardTracker.stop()

                withContext(Dispatchers.IO) {
                    AppDatabase.getInstance(requireContext()).favoriteDao().deleteAll()
                }

                val intent = Intent(requireContext(), WelcomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            } catch (e: Exception) {
                btnDeleteAccount.isEnabled = true
                btnDeleteAccount.text = "Delete Account"
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Could not delete account. Please try again.\n${e.message}")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}
