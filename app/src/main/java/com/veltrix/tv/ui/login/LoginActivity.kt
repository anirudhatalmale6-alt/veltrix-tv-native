package com.veltrix.tv.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.veltrix.tv.R
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.api.XtreamApiService
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.util.ensureHttp
import com.veltrix.tv.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.veltrix.tv.data.CustomerPrefsManager

class LoginActivity : AppCompatActivity() {

    private lateinit var etServerUrl: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvIptvLink: TextView
    private lateinit var prefs: PrefsManager
    private lateinit var customerPrefs: CustomerPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = PrefsManager.getInstance(this)
        customerPrefs = CustomerPrefsManager.getInstance(this)

        if (prefs.isLoggedIn) {
            com.veltrix.tv.util.DashboardTracker.init(this, prefs.username, "1.0.47")
            navigateToMain()
            return
        }

        etServerUrl = findViewById(R.id.etServerUrl)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        tvIptvLink = findViewById(R.id.tvIptvLink)

        // Show the IPTV fallback link only if customer is logged in via the new flow
        if (customerPrefs.isLoggedIn) {
            tvIptvLink.visibility = View.VISIBLE
        }

        etServerUrl.setText(prefs.serverUrl)
        etUsername.setText(prefs.username)

        etServerUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etUsername.requestFocus()
                true
            } else false
        }

        etUsername.setOnEditorActionListener { _, actionId, _ ->
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

        btnLogin.setOnClickListener { performLogin() }
        btnLogin.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }

        etServerUrl.requestFocus()
    }

    private fun performLogin() {
        val serverUrl = etServerUrl.text.toString().trim().ensureHttp()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (serverUrl.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields")
            return
        }

        setLoading(true)
        tvError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val baseUrl = serverUrl.trimEnd('/') + "/"
                val client = MainActivity.createHttpClient(15, 15)

                val api = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(XtreamApiService::class.java)

                val response = withContext(Dispatchers.IO) {
                    api.authenticate(username, password)
                }

                val userInfo = response.userInfo
                val serverInfo = response.serverInfo

                if (userInfo?.status == "Active" || userInfo?.auth == 1) {
                    prefs.serverUrl = serverUrl
                    prefs.username = username
                    prefs.password = password
                    prefs.port = serverInfo?.port ?: ""
                    prefs.isLoggedIn = true

                    com.veltrix.tv.util.DashboardTracker.init(this@LoginActivity, username, "1.0.47")
                    navigateToMain()
                } else if (userInfo?.status == "Expired") {
                    showError(getString(R.string.account_expired))
                } else if (userInfo?.status == "Disabled") {
                    showError(getString(R.string.account_disabled))
                } else {
                    showError(getString(R.string.login_failed))
                }
            } catch (e: Exception) {
                showError(getString(R.string.connection_error) + "\n${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        btnLogin.isEnabled = !loading
        etServerUrl.isEnabled = !loading
        etUsername.isEnabled = !loading
        etPassword.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
