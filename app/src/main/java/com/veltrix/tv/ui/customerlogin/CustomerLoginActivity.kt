package com.veltrix.tv.ui.customerlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.veltrix.tv.R
import com.veltrix.tv.data.CustomerPrefsManager
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.ui.login.LoginActivity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.packages.PackageActivity
import com.veltrix.tv.ui.register.RegisterActivity
import com.veltrix.tv.util.DashboardTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CustomerLoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvRegister: TextView
    private lateinit var customerPrefs: CustomerPrefsManager
    private lateinit var iptvPrefs: PrefsManager

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        customerPrefs = CustomerPrefsManager.getInstance(this)
        iptvPrefs = PrefsManager.getInstance(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        tvRegister = findViewById(R.id.tvRegister)

        // Pre-fill email if we have it stored
        if (customerPrefs.customerEmail.isNotEmpty()) {
            etEmail.setText(customerPrefs.customerEmail)
        }

        etEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etPassword.requestFocus()
                true
            } else false
        }

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performLogin()
                true
            } else false
        }

        btnSignIn.setOnClickListener { performLogin() }
        btnSignIn.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        etEmail.requestFocus()
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.error_email_empty))
            return
        }

        setLoading(true)
        tvError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    postLogin(email, password)
                }

                if (result.success) {
                    customerPrefs.saveSession(
                        email = email,
                        token = result.token,
                        id = result.customerId,
                        status = result.subscriptionStatus,
                        expiry = result.subscriptionExpiry
                    )

                    DashboardTracker.init(this@CustomerLoginActivity, email, "1.0.47")

                    when {
                        // Active subscription and IPTV credentials already stored → go to main
                        customerPrefs.hasActiveSubscription && iptvPrefs.isLoggedIn -> {
                            navigateTo(MainActivity::class.java)
                        }
                        // Active subscription but no IPTV login yet → go to IPTV login
                        customerPrefs.hasActiveSubscription -> {
                            navigateTo(LoginActivity::class.java)
                        }
                        // No active subscription → go to package selection
                        else -> {
                            navigateTo(PackageActivity::class.java)
                        }
                    }
                } else {
                    showError(result.errorMessage.ifEmpty { getString(R.string.error_login_failed) })
                }
            } catch (e: Exception) {
                showError(getString(R.string.connection_error) + "\n${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun postLogin(email: String, password: String): LoginResult {
        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${DashboardTracker.BASE_URL}/api/customer/login")
            .header("X-API-Key", DashboardTracker.API_KEY)
            .post(body)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        return if (response.isSuccessful) {
            try {
                val obj = JSONObject(responseBody)
                val data = obj.optJSONObject("data")
                LoginResult(
                    success = true,
                    token = data?.optString("token", "") ?: "",
                    customerId = data?.optString("id", "") ?: "",
                    subscriptionStatus = data?.optString("subscription_status", CustomerPrefsManager.STATUS_NONE) ?: CustomerPrefsManager.STATUS_NONE,
                    subscriptionExpiry = data?.optString("subscription_expiry", "") ?: ""
                )
            } catch (e: Exception) {
                LoginResult(success = true, token = "", customerId = "")
            }
        } else {
            val errorMsg = try {
                JSONObject(responseBody).optString("message", "")
            } catch (e: Exception) { "" }
            LoginResult(success = false, errorMessage = errorMsg)
        }
    }

    private fun setLoading(loading: Boolean) {
        btnSignIn.isEnabled = !loading
        etEmail.isEnabled = !loading
        etPassword.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun navigateTo(cls: Class<*>) {
        startActivity(Intent(this, cls))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private data class LoginResult(
        val success: Boolean,
        val token: String = "",
        val customerId: String = "",
        val subscriptionStatus: String = CustomerPrefsManager.STATUS_NONE,
        val subscriptionExpiry: String = "",
        val errorMessage: String = ""
    )
}
