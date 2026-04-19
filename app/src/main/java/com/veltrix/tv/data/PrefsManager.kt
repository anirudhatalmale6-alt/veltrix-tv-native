package com.veltrix.tv.data

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("veltrix_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SERVER_URL = "server_url"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PORT = "port"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_LAST_CATEGORY = "last_category"
        private const val KEY_LAST_SECTION = "last_section"

        @Volatile
        private var INSTANCE: PrefsManager? = null

        fun getInstance(context: Context): PrefsManager {
            return INSTANCE ?: synchronized(this) {
                val instance = PrefsManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    var serverUrl: String
        get() = prefs.getString(KEY_SERVER_URL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SERVER_URL, value).apply()

    var username: String
        get() = prefs.getString(KEY_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USERNAME, value).apply()

    var password: String
        get() = prefs.getString(KEY_PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PASSWORD, value).apply()

    var port: String
        get() = prefs.getString(KEY_PORT, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PORT, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()

    var lastCategory: String
        get() = prefs.getString(KEY_LAST_CATEGORY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_CATEGORY, value).apply()

    var lastSection: Int
        get() = prefs.getInt(KEY_LAST_SECTION, 0)
        set(value) = prefs.edit().putInt(KEY_LAST_SECTION, value).apply()

    fun getBaseUrl(): String {
        val url = serverUrl.trimEnd('/')
        return if (port.isNotEmpty()) "$url:$port" else url
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
