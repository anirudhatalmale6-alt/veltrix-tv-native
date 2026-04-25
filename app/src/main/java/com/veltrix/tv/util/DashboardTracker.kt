package com.veltrix.tv.util

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object DashboardTracker {

    private const val BASE_URL = "https://downloads-bias-alone-union.trycloudflare.com"
    private const val API_KEY = "veltrix-track-2026"
    private const val HEARTBEAT_INTERVAL = 60_000L

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val handler = Handler(Looper.getMainLooper())
    private var heartbeatRunnable: Runnable? = null

    private var iptvUsername: String = ""
    private var deviceId: String = ""
    private var deviceName: String = ""
    private var deviceType: String = "phone"
    private var appVersion: String = ""
    private var currentChannel: String = ""
    private var currentCategory: String = ""
    private var currentWatchId: Long = -1

    fun init(context: Context, username: String, version: String) {
        iptvUsername = username
        appVersion = version
        deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
        deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
        deviceType = if (context.packageManager.hasSystemFeature("android.software.leanback")) "tv"
                     else if (context.resources.configuration.smallestScreenWidthDp >= 600) "tablet"
                     else "phone"

        track("login")
        startHeartbeat()
    }

    fun onWatchStart(channelName: String, category: String = "") {
        currentChannel = channelName
        currentCategory = category
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = buildJson("watch_start").apply {
                    put("channel_name", channelName)
                    put("channel_category", category)
                }
                val resp = post(json)
                if (resp != null) {
                    val data = JSONObject(resp).optJSONObject("data")
                    currentWatchId = data?.optLong("watch_id", -1) ?: -1
                }
            } catch (_: Exception) {}
        }
    }

    fun onWatchStop() {
        val watchId = currentWatchId
        currentChannel = ""
        currentCategory = ""
        currentWatchId = -1
        if (watchId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val json = buildJson("watch_stop").apply {
                        put("watch_id", watchId)
                    }
                    post(json)
                } catch (_: Exception) {}
            }
        }
    }

    fun stop() {
        heartbeatRunnable?.let { handler.removeCallbacks(it) }
        heartbeatRunnable = null
        onWatchStop()
    }

    private fun startHeartbeat() {
        heartbeatRunnable?.let { handler.removeCallbacks(it) }
        val runnable = object : Runnable {
            override fun run() {
                track("heartbeat")
                handler.postDelayed(this, HEARTBEAT_INTERVAL)
            }
        }
        heartbeatRunnable = runnable
        handler.postDelayed(runnable, HEARTBEAT_INTERVAL)
    }

    private fun track(action: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = buildJson(action).apply {
                    if (currentChannel.isNotEmpty()) {
                        put("channel_name", currentChannel)
                        put("channel_category", currentCategory)
                    }
                }
                post(json)
            } catch (_: Exception) {}
        }
    }

    private fun buildJson(action: String): JSONObject {
        return JSONObject().apply {
            put("action", action)
            put("iptv_username", iptvUsername)
            put("device_id", deviceId)
            put("device_name", deviceName)
            put("device_type", deviceType)
            put("app_version", appVersion)
        }
    }

    private fun post(json: JSONObject): String? {
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/api/track")
            .header("X-API-Key", API_KEY)
            .post(body)
            .build()
        return try {
            client.newCall(request).execute().use { it.body?.string() }
        } catch (_: Exception) { null }
    }
}
