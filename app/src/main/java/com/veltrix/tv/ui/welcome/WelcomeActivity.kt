package com.veltrix.tv.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.veltrix.tv.R
import com.veltrix.tv.data.CustomerPrefsManager
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.ui.login.LoginActivity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.register.RegisterActivity
import com.veltrix.tv.ui.customerlogin.CustomerLoginActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var customerPrefs: CustomerPrefsManager
    private lateinit var iptvPrefs: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customerPrefs = CustomerPrefsManager.getInstance(this)
        iptvPrefs = PrefsManager.getInstance(this)

        // If IPTV already logged in (returning user with active session), go straight to main
        if (iptvPrefs.isLoggedIn) {
            com.veltrix.tv.util.DashboardTracker.init(this, iptvPrefs.username, "1.0.49")
            navigateTo(MainActivity::class.java)
            return
        }

        setContentView(R.layout.activity_welcome)

        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val tvIptvDirect = findViewById<TextView>(R.id.tvIptvDirect)

        btnCreateAccount.setOnClickListener {
            navigateTo(RegisterActivity::class.java)
        }

        btnSignIn.setOnClickListener {
            navigateTo(CustomerLoginActivity::class.java)
        }

        tvIptvDirect.setOnClickListener {
            navigateTo(LoginActivity::class.java)
        }

        btnCreateAccount.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }
        btnSignIn.setOnFocusChangeListener { v, hasFocus -> v.isSelected = hasFocus }
        tvIptvDirect.setOnFocusChangeListener { v, hasFocus ->
            (v as TextView).setTextColor(
                if (hasFocus) resources.getColor(R.color.cyan, null)
                else resources.getColor(R.color.text_dim, null)
            )
        }

        btnCreateAccount.requestFocus()
    }

    private fun navigateTo(cls: Class<*>) {
        startActivity(Intent(this, cls))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
