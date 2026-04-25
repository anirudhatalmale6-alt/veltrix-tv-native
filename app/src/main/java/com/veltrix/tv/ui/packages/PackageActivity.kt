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
import com.veltrix.tv.util.DashboardTracker
import com.veltrix.tv.util.toast
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

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Stripe payment links
    private val STRIPE_MONTHLY   = "https://buy.stripe.com/aFa6oHdq893SdAVfQD8Vi02"
    private val STRIPE_3_MONTHS  = "https://buy.stripe.com/4gMbJ1gCk7ZOdAVfQD8Vi01"
    private val STRIPE_6_MONTHS  = "https://buy.stripe.com/cNibJ11Hqbc08gB6g38Vi04"

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

        btnSelectMonthly.setOnClickListener  { openStripePayment(STRIPE_MONTHLY) }
        btnSelect3Months.setOnClickListener  { openStripePayment(STRIPE_3_MONTHS) }
        btnSelect6Months.setOnClickListener  { openStripePayment(STRIPE_6_MONTHS) }
        btnStartTrial.setOnClickListener     { activateTrial() }

        btnSelectMonthly.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnSelect3Months.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnSelect6Months.setOnFocusChangeListener  { v, hasFocus -> v.isSelected = hasFocus }
        btnStartTrial.setOnFocusChangeListener     { v, hasFocus -> v.isSelected = hasFocus }

        btnSelectMonthly.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        // When returning from browser after payment, check subscription status
        if (customerPrefs.customerToken.isNotEmpty()) {
            checkSubscriptionStatus()
        }
    }

    private fun openStripePayment(baseUrl: String) {
        val email = customerPrefs.customerEmail
        val customerId = customerPrefs.customerId
        val urlBuilder = StringBuilder(baseUrl)

        if (email.isNotEmpty() || customerId.isNotEmpty()) {
            urlBuilder.append("?")
            if (email.isNotEmpty()) {
                urlBuilder.append("prefilled_email=")
                urlBuilder.append(Uri.encode(email))
            }
            if (customerId.isNotEmpty()) {
                if (email.isNotEmpty()) urlBuilder.append("&")
                urlBuilder.append("client_reference_id=")
                urlBuilder.append(Uri.encode(customerId))
            }
        }

        showStatus(getString(R.string.opening_payment))
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlBuilder.toString()))
            startActivity(intent)
        } catch (e: Exception) {
            toast(getString(R.string.connection_error))
        }
    }

    private fun activateTrial() {
        val token = customerPrefs.customerToken
        if (token.isEmpty()) {
            toast(getString(R.string.error_login_failed))
            return
        }

        setLoading(true)
        showStatus(getString(R.string.trial_activating))

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    postActivateTrial(token)
                }

                if (result) {
                    customerPrefs.subscriptionStatus = CustomerPrefsManager.STATUS_TRIAL
                    toast(getString(R.string.trial_activated))
                    navigateToIptvLogin()
                } else {
                    showStatus(getString(R.string.error_register_failed))
                }
            } catch (e: Exception) {
                showStatus(getString(R.string.connection_error) + "\n${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun postActivateTrial(token: String): Boolean {
        val json = JSONObject().apply {
            put("email", customerPrefs.customerEmail)
        }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${DashboardTracker.BASE_URL}/api/customer/trial")
            .header("X-API-Key", DashboardTracker.API_KEY)
            .post(body)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
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
}
