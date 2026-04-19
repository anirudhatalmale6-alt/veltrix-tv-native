package com.veltrix.tv

import android.app.Application
import android.util.Log
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VeltrixApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Configure image loading with caching for faster browsing
        val imageLoader = ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // 25% of app memory
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
