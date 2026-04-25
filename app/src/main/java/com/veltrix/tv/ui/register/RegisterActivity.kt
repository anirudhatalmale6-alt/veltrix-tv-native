package com.veltrix.tv.ui.register

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
import com.veltrix.tv.ui.customerlogin.CustomerLoginActivity
import com.veltrix.tv.ui.packages.PackageActivity
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

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnCreateAccount: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvSignIn: TextView
    private lateinit var customerPrefs: CustomerPrefsManager

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        customerPrefs = CustomerPrefsManager.getInstance(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        tvSignIn = findViewById(R.id.tvSignIn)

        etEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etPassword.requestFocus()
                true
            } else false
        }

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etConfirmPassword.requestFocus()
                true
            } else false
        }

        etConfirmPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performRegister()
                true
            } else false
        }

        btnCreateAccount.setOnClickListener { performRegister() }
        btnCreateAccount.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }

        tvSignIn.setOnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        etEmail.requestFocus()
    }

    private fun performRegister() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (email.isEmpty()) {
            showError(getString(R.string.error_email_empty))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.error_email_invalid))
            return
        }

        if (password.isEmpty()) {
            showError(getString(R.string.error_password_empty))
            return
        }

        if (password.length < 6) {
            showError(getString(R.string.error_password_short))
            return
        }

        if (password != confirmPassword) {
            showError(getString(R.string.error_passwords_mismatch))
            return
        }

        setLoading(true)
        tvError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    postRegister(email, password)
                }

                if (result.success) {
                    customerPrefs.saveSession(
                        email = email,
                        token = result.token,
                        id = result.customerId,
                        status = CustomerPrefsManager.STATUS_PENDING,
                        expiry = ""
                    )
                    DashboardTracker.init(this@RegisterActivity, email, "1.0.47")
                    startActivity(Intent(this@RegisterActivity, PackageActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                } else {
                    showError(result.errorMessage.ifEmpty { getString(R.string.error_register_failed) })
                }
            } catch (e: Exception) {
                showError(getString(R.string.connection_error) + "\n${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun postRegister(email: String, password: String): RegisterResult {
        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${DashboardTracker.BASE_URL}/api/customer/register")
            .header("X-API-Key", DashboardTracker.API_KEY)
            .post(body)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        return if (response.isSuccessful) {
            try {
                val obj = JSONObject(responseBody)
                val data = obj.optJSONObject("data")
                RegisterResult(
                    success = true,
                    token = data?.optString("token", "") ?: "",
                    customerId = data?.optString("id", "") ?: ""
                )
            } catch (e: Exception) {
                RegisterResult(success = true, token = "", customerId = "")
            }
        } else {
            val errorMsg = try {
                JSONObject(responseBody).optString("message", "")
            } catch (e: Exception) { "" }
            RegisterResult(success = false, errorMessage = errorMsg)
        }
    }

    private fun setLoading(loading: Boolean) {
        btnCreateAccount.isEnabled = !loading
        etEmail.isEnabled = !loading
        etPassword.isEnabled = !loading
        etConfirmPassword.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private data class RegisterResult(
        val success: Boolean,
        val token: String = "",
        val customerId: String = "",
        val errorMessage: String = ""
    )
}
