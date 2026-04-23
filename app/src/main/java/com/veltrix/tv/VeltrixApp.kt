package com.veltrix.tv

import android.app.Application
import android.util.Log
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.io.File
import java.net.Inet4Address
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

        // DNS-over-HTTPS for WiFi networks where ISP DNS blocks IPTV domains
        val bootstrapClient = OkHttpClient.Builder().build()
        val googleDoH = DnsOverHttps.Builder()
            .client(bootstrapClient)
            .url("https://dns.google/dns-query".toHttpUrl())
            .bootstrapDnsHosts(InetAddress.getByName("8.8.8.8"), InetAddress.getByName("8.8.4.4"))
            .build()
        val cloudflareDoH = DnsOverHttps.Builder()
            .client(bootstrapClient)
            .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
            .bootstrapDnsHosts(InetAddress.getByName("1.1.1.1"), InetAddress.getByName("1.0.0.1"))
            .build()

        val iptvDns = object : Dns {
            private fun preferIPv4(addrs: List<InetAddress>): List<InetAddress> {
                val v4 = addrs.filter { it is Inet4Address }
                return if (v4.isNotEmpty()) v4 else addrs
            }

            override fun lookup(hostname: String): List<InetAddress> {
                try {
                    val r = Dns.SYSTEM.lookup(hostname)
                    if (r.isNotEmpty()) return preferIPv4(r)
                } catch (_: Exception) {}
                try {
                    val r = googleDoH.lookup(hostname)
                    if (r.isNotEmpty()) return preferIPv4(r)
                } catch (_: Exception) {}
                try {
                    val r = cloudflareDoH.lookup(hostname)
                    if (r.isNotEmpty()) return preferIPv4(r)
                } catch (_: Exception) {}
                return emptyList()
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
