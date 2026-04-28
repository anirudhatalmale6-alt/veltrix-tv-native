package com.veltrix.tv.ui.packages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.veltrix.tv.R
import com.veltrix.tv.data.CustomerPrefsManager
import com.veltrix.tv.ui.login.LoginActivity
import com.veltrix.tv.ui.welcome.WelcomeActivity
import com.veltrix.tv.util.DashboardTracker
import com.veltrix.tv.util.toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class PackageActivity : AppCompatActivity() {

    private lateinit var btnSelectMonthly: Button
    private lateinit var btnSelect3Months: Button
    private lateinit var btnSelect6Months: Button
    private lateinit var btnStartTrial: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvStatus: TextView
    private lateinit var customerPrefs: CustomerPrefsManager
    private var openedStripePayment = false

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)

        customerPrefs = CustomerPrefsManager.getInstance(this)

        btnSelectMonthly  = findViewById(R.id.btnSelectMonthly)
        btnSelect3Months  = findViewById(R.id.btnSelect3Months)
        btnSelect6Months  = findViewById(R.id.btnSelect6Months)
        btnStartTrial     = findViewById(R.id.btnStartTrial)
        progressBar       = findViewById(R.id.progressBar)
        tvStatus          = findViewById(R.id.tvStatus)

        setupCardFocus()

        btnSelectMonthly.setOnClickListener  { startPayPalCheckout("monthly") }
        btnSelect3Months.setOnClickListener  { startPayPalCheckout("3month") }
        btnSelect6Months.setOnClickListener  { startPayPalCheckout("6month") }
        btnStartTrial.setOnClickListener     { activateTrial() }

        btnSelectMonthly.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnSelect3Months.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnSelect6Months.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnStartTrial.setOnFocusChangeListener     { v, hasFocus -> v.isSelected = hasFocus }

        val tvDeleteAccount = findViewById<TextView>(R.id.tvDeleteAccount)
        tvDeleteAccount.setOnClickListener { showDeleteAccountDialog() }
        tvDeleteAccount.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }

        btnSelectMonthly.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        if (openedStripePayment && customerPrefs.customerToken.isNotEmpty()) {
            openedStripePayment = false
            checkSubscriptionStatus()
        }
    }

    private fun startPayPalCheckout(plan: String) {
        val email = customerPrefs.customerEmail
        if (email.isEmpty()) {
            toast(getString(R.string.error_login_failed))
            return
        }

        setLoading(true)
        showStatus(getString(R.string.opening_payment))

        lifecycleScope.launch {
            try {
                val checkoutUrl = withContext(Dispatchers.IO) {
                    val json = JSONObject().apply {
                        put("plan", plan)
                        put("email", email)
                    }
                    val body = json.toString().toRequestBody("application/json".toMediaType())
                    val request = Request.Builder()
                        .url("${DashboardTracker.BASE_URL}/api/checkout")
                        .header("X-API-Key", DashboardTracker.API_KEY)
                        .post(body)
                        .build()

                    val response = httpClient.newCall(request).execute()
                    val respBody = response.body?.string() ?: ""
                    val obj = JSONObject(respBody)
                    if (obj.optBoolean("success")) {
                        obj.optJSONObject("data")?.optString("checkout_url", "") ?: ""
                    } else {
                        throw Exception(obj.optString("error", "Checkout failed"))
                    }
                }

                if (checkoutUrl.isNotEmpty()) {
                    openedStripePayment = true
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkoutUrl))
                    startActivity(intent)
                } else {
                    showStatus("Could not create checkout")
                }
            } catch (e: Exception) {
                showStatus(e.message ?: getString(R.string.connection_error))
            } finally {
                setLoading(false)
            }
        }
    }

    private fun activateTrial() {
        val email = customerPrefs.customerEmail
        if (email.isEmpty()) {
            toast(getString(R.string.error_login_failed))
            return
        }

        setLoading(true)
        showStatus("Creating your IPTV account (30s)...")

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    postActivateTrial(email)
                }

                if (result.success) {
                    customerPrefs.subscriptionStatus = CustomerPrefsManager.STATUS_TRIAL
                    if (result.username.isNotEmpty() && result.password.isNotEmpty()) {
                        showCredentialsDialog(result.server, result.username, result.password)
                    } else {
                        toast(getString(R.string.trial_activated))
                        navigateToIptvLogin()
                    }
                } else {
                    showStatus(result.error)
                }
            } catch (e: Exception) {
                showStatus(getString(R.string.connection_error) + "\n${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    private data class TrialResult(
        val success: Boolean,
        val error: String = "",
        val server: String = "",
        val username: String = "",
        val password: String = ""
    )

    private fun postActivateTrial(email: String): TrialResult {
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${DashboardTracker.BASE_URL}/api/customer/trial")
            .header("X-API-Key", DashboardTracker.API_KEY)
            .post(body)
            .build()

        val response = httpClient.newCall(request).execute()
        val respBody = response.body?.string() ?: ""
        val obj = JSONObject(respBody)

        if (response.isSuccessful && obj.optBoolean("success")) {
            val data = obj.optJSONObject("data")
            return TrialResult(
                success = true,
                server = data?.optString("iptv_server", "http://veltrixstream.space") ?: "http://veltrixstream.space",
                username = data?.optString("iptv_username", "") ?: "",
                password = data?.optString("iptv_password", "") ?: ""
            )
        }

        return TrialResult(success = false, error = obj.optString("error", "Trial activation failed"))
    }

    private fun showCredentialsDialog(server: String, username: String, password: String) {
        val iptvPrefs = com.veltrix.tv.data.PrefsManager.getInstance(this)
        iptvPrefs.serverUrl = server
        iptvPrefs.username = username
        iptvPrefs.password = password
        iptvPrefs.isLoggedIn = true

        AlertDialog.Builder(this)
            .setTitle("Trial Activated!")
            .setMessage("Your IPTV credentials:\n\nServer: $server\nUsername: $username\nPassword: $password\n\nCredentials have been saved. You can start watching now!")
            .setPositiveButton("Start Watching") { _, _ ->
                DashboardTracker.init(this, username, "1.0.64")
                val intent = Intent(this, com.veltrix.tv.ui.main.MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun checkSubscriptionStatus() {
        val token = customerPrefs.customerToken
        if (token.isEmpty()) return

        lifecycleScope.launch {
            try {
                val status = withContext(Dispatchers.IO) {
                    fetchSubscriptionStatus(token)
                }
                if (status == CustomerPrefsManager.STATUS_ACTIVE || status == CustomerPrefsManager.STATUS_TRIAL) {
                    customerPrefs.subscriptionStatus = status
                    navigateToIptvLogin()
                }
                // If still pending/expired, stay on this screen
            } catch (e: Exception) {
                // Silently ignore — user may not have completed payment yet
            }
        }
    }

    private fun fetchSubscriptionStatus(token: String): String {
        val json = JSONObject().apply {
            put("email", customerPrefs.customerEmail)
        }
        val reqBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${DashboardTracker.BASE_URL}/api/customer/subscription")
            .header("X-API-Key", DashboardTracker.API_KEY)
            .post(reqBody)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return CustomerPrefsManager.STATUS_NONE

        val body = response.body?.string() ?: return CustomerPrefsManager.STATUS_NONE
        return try {
            val obj = JSONObject(body)
            val data = obj.optJSONObject("data")
            val status = data?.optString("status", CustomerPrefsManager.STATUS_NONE) ?: CustomerPrefsManager.STATUS_NONE
            val expiry = data?.optString("subscription_expires_at", "") ?: ""
            if (expiry.isNotEmpty()) customerPrefs.subscriptionExpiry = expiry
            status
        } catch (e: Exception) {
            CustomerPrefsManager.STATUS_NONE
        }
    }

    private fun setLoading(loading: Boolean) {
        btnSelectMonthly.isEnabled = !loading
        btnSelect3Months.isEnabled = !loading
        btnSelect6Months.isEnabled = !loading
        btnStartTrial.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showStatus(message: String) {
        tvStatus.text = message
        tvStatus.visibility = View.VISIBLE
    }

    private fun setupCardFocus() {
        val cardMonthly  = findViewById<LinearLayout>(R.id.cardMonthly)
        val card3Months  = findViewById<LinearLayout>(R.id.card3Months)
        val card6Months  = findViewById<LinearLayout>(R.id.card6Months)
        val cardTrial    = findViewById<LinearLayout>(R.id.cardTrial)

        listOf(cardMonthly, card3Months, card6Months, cardTrial).forEach { card ->
            card.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }
        }
    }

    private fun navigateToIptvLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("This will permanently delete your account. This cannot be undone.\n\nAre you sure?")
            .setPositiveButton("Delete") { _, _ ->
                performDeleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performDeleteAccount() {
        setLoading(true)
        showStatus("Deleting account...")

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val json = JSONObject().apply {
                        put("email", customerPrefs.customerEmail)
                    }
                    val body = json.toString().toRequestBody("application/json".toMediaType())
                    val request = Request.Builder()
                        .url("${DashboardTracker.BASE_URL}/api/customer/delete")
                        .header("X-API-Key", DashboardTracker.API_KEY)
                        .post(body)
                        .build()
                    httpClient.newCall(request).execute()
                }

                customerPrefs.clear()
                toast("Account deleted")

                val intent = Intent(this@PackageActivity, WelcomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                showStatus("Could not delete account. Please try again.")
            } finally {
                setLoading(false)
            }
        }
    }
}
