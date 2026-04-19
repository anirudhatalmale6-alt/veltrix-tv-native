package com.veltrix.tv

import android.app.Application
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VeltrixApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val crashLog = sw.toString()
                Log.e("VeltrixTV", "CRASH: $crashLog")

                // Save crash log to file for debugging
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val crashFile = File(getExternalFilesDir(null), "crash_$timestamp.txt")
                crashFile.writeText("Crash at $timestamp\nThread: ${thread.name}\n\n$crashLog")
            } catch (_: Exception) {
                // Ignore errors in crash handler
            }

            // Call default handler to let the system handle it
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
