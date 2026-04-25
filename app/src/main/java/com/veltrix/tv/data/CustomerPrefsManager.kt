package com.veltrix.tv.data

import android.content.Context
import android.content.SharedPreferences

class CustomerPrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("veltrix_customer_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "customer_email"
        private const val KEY_TOKEN = "customer_token"
        private const val KEY_CUSTOMER_ID = "customer_id"
        private const val KEY_SUBSCRIPTION_STATUS = "subscription_status"
        private const val KEY_SUBSCRIPTION_EXPIRY = "subscription_expiry"

        const val STATUS_ACTIVE = "active"
        const val STATUS_TRIAL = "trial"
        const val STATUS_PENDING = "pending"
        const val STATUS_EXPIRED = "expired"
        const val STATUS_NONE = ""

        @Volatile
        private var INSTANCE: CustomerPrefsManager? = null

        fun getInstance(context: Context): CustomerPrefsManager {
            return INSTANCE ?: synchronized(this) {
                val instance = CustomerPrefsManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    var customerEmail: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    var customerToken: String
        get() = prefs.getString(KEY_TOKEN, "") ?: ""
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var customerId: String
        get() = prefs.getString(KEY_CUSTOMER_ID, "") ?: ""
        set(value) = prefs.edit().putString(KEY_CUSTOMER_ID, value).apply()

    var subscriptionStatus: String
        get() = prefs.getString(KEY_SUBSCRIPTION_STATUS, STATUS_NONE) ?: STATUS_NONE
        set(value) = prefs.edit().putString(KEY_SUBSCRIPTION_STATUS, value).apply()

    var subscriptionExpiry: String
        get() = prefs.getString(KEY_SUBSCRIPTION_EXPIRY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SUBSCRIPTION_EXPIRY, value).apply()

    val isLoggedIn: Boolean
        get() = customerToken.isNotEmpty()

    val hasActiveSubscription: Boolean
        get() = subscriptionStatus == STATUS_ACTIVE || subscriptionStatus == STATUS_TRIAL

    fun saveSession(email: String, token: String, id: String, status: String, expiry: String) {
        customerEmail = email
        customerToken = token
        customerId = id
        subscriptionStatus = status
        subscriptionExpiry = expiry
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
