package com.veltrix.tv

import android.app.Application
import android.util.Log
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.io.File
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VeltrixApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // OkHttp client with IPTV-compatible user-agent + DNS fallback for image loading
        val iptvDns = object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                return try {
                    val result = Dns.SYSTEM.lookup(hostname)
                    if (result.isNotEmpty()) result else InetAddress.getAllByName(hostname).toList()
                } catch (_: Exception) {
                    try { InetAddress.getAllByName(hostname).toList() } catch (_: Exception) { emptyList() }
                }
            }
        }
        val okHttpClient = OkHttpClient.Builder()
            .dns(iptvDns)
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Lavf/60.3.100")
                    .header("Connection", "keep-alive")
                    .header("Accept", "*/*")
                    .build()
                chain.proceed(request)
            }
            .build()

        // Configure image loading with caching for faster browsing
        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.10) // 10% of app memory (reduced for TV)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(cacheDir, "image_cache"))
                    .maxSizePercent(0.10) // 10% of disk space
                    .build()
            }
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val crashLog = sw.toString()
                Log.e("VeltrixTV", "CRASH: $crashLog")

                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val crashFile = File(getExternalFilesDir(null), "crash_$timestamp.txt")
                crashFile.writeText("Crash at $timestamp\nThread: ${thread.name}\n\n$crashLog")
            } catch (_: Exception) {}

            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
