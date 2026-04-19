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
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.ui.login.LoginActivity
import com.veltrix.tv.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment() {

    private lateinit var tvServer: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvExpiry: TextView
    private lateinit var tvMaxConn: TextView
    private lateinit var btnLogout: Button

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
        AlertDialog.Builder(requireContext(), R.style.AppTheme)
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
}
