package com.veltrix.tv.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.veltrix.tv.R
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.api.XtreamApiService
import com.veltrix.tv.ui.favorites.FavoritesFragment
import com.veltrix.tv.ui.history.HistoryFragment
import com.veltrix.tv.ui.live.LiveFragment
import com.veltrix.tv.ui.search.SearchFragment
import com.veltrix.tv.ui.series.SeriesFragment
import com.veltrix.tv.ui.settings.SettingsFragment
import com.veltrix.tv.ui.vod.VodFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var sidebarFragment: SidebarFragment
    private lateinit var prefs: PrefsManager
    private lateinit var contentContainer: FrameLayout
    private lateinit var sidebarContainer: FrameLayout
    private lateinit var tvDebug: TextView
    private var currentContentFragment: Fragment? = null
    private var exitConfirmed = false

    companion object {
        lateinit var apiService: XtreamApiService
            private set
        lateinit var prefsInstance: PrefsManager
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = PrefsManager.getInstance(this)
        prefsInstance = prefs

        sidebarContainer = findViewById(R.id.sidebarContainer)
        contentContainer = findViewById(R.id.contentContainer)
        tvDebug = findViewById(R.id.tvDebug)

        initApi()
        setupSidebar()
        debug("Ready. Use remote to navigate.")
    }

    private fun debug(msg: String) {
        try {
            android.util.Log.d("VeltrixTV", msg)
        } catch (_: Exception) {}
    }

    private fun initApi() {
        val baseUrl = prefs.getBaseUrl().trimEnd('/') + "/"
        val cacheDir = java.io.File(cacheDir, "http_cache")
        val cache = okhttp3.Cache(cacheDir, 50L * 1024 * 1024) // 50MB cache
        val client = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XtreamApiService::class.java)
    }

    private fun setupSidebar() {
        sidebarFragment = SidebarFragment()
        sidebarFragment.onSectionSelected = { item, _ ->
            debug("Sidebar: ${item.title}")
            switchContent(item.id)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.sidebarContainer, sidebarFragment)
            .commitAllowingStateLoss()

        switchContent(SidebarFragment.ID_LIVE)
    }

    private fun switchContent(sectionId: Int) {
        try {
            val fragment: Fragment = when (sectionId) {
                SidebarFragment.ID_LIVE -> LiveFragment()
                SidebarFragment.ID_MOVIES -> VodFragment()
                SidebarFragment.ID_SERIES -> SeriesFragment()
                SidebarFragment.ID_SEARCH -> SearchFragment()
                SidebarFragment.ID_HISTORY -> HistoryFragment()
                SidebarFragment.ID_FAVORITES -> FavoritesFragment()
                SidebarFragment.ID_SETTINGS -> SettingsFragment()
                else -> LiveFragment()
            }

            currentContentFragment = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            debug("switchContent error: ${e.message}")
        }
    }

    private fun isFocusInSidebar(): Boolean {
        val focused = currentFocus ?: return false
        return isDescendantOf(focused, sidebarContainer)
    }

    private fun isFocusInContent(): Boolean {
        val focused = currentFocus ?: return false
        return isDescendantOf(focused, contentContainer)
    }

    private fun isDescendantOf(child: android.view.View, parent: android.view.View): Boolean {
        var current: android.view.ViewParent? = child.parent
        while (current != null) {
            if (current === parent) return true
            current = current.parent
        }
        return false
    }

    private fun focusFirstInContent() {
        contentContainer.post {
            try {
                val firstFocusable = contentContainer.findFocus()
                    ?: findFirstFocusable(contentContainer)
                firstFocusable?.requestFocus()
            } catch (e: Exception) {
                debug("focusFirst error: ${e.message}")
            }
        }
    }

    private fun findFirstFocusable(view: android.view.View): android.view.View? {
        if (view.isFocusable && view !is FrameLayout) return view
        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                val found = findFirstFocusable(view.getChildAt(i))
                if (found != null) return found
            }
        }
        return null
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Log all key events for debugging
        if (event.action == KeyEvent.ACTION_DOWN) {
            val keyName = KeyEvent.keyCodeToString(event.keyCode)
            val focusArea = when {
                isFocusInSidebar() -> "sidebar"
                isFocusInContent() -> "content"
                else -> "unknown"
            }
            debug("Key: $keyName | Focus: $focusArea")
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        try {
            when (keyCode) {
                // SELECT/OK buttons - let them pass through to click handlers
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER,
                KeyEvent.KEYCODE_NUMPAD_ENTER, KeyEvent.KEYCODE_BUTTON_A -> {
                    return super.onKeyDown(keyCode, event)
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (isFocusInSidebar()) {
                        focusFirstInContent()
                        return true
                    }
                }
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    if (isFocusInContent()) {
                        val frag = currentContentFragment
                        if (frag is DpadNavigable && frag.canGoLeft()) {
                            return super.onKeyDown(keyCode, event)
                        }
                        sidebarFragment.focusCurrentItem()
                        return true
                    }
                }
                KeyEvent.KEYCODE_BACK -> {
                    if (isFocusInContent()) {
                        sidebarFragment.focusCurrentItem()
                        return true
                    }
                    showExitDialog()
                    return true
                }
            }
        } catch (e: Exception) {
            debug("onKeyDown error: ${e.message}")
        }

        return super.onKeyDown(keyCode, event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Safety net: prevent accidental back press from closing the app
        if (isFocusInContent()) {
            sidebarFragment.focusCurrentItem()
            return
        }
        showExitDialog()
    }

    override fun finish() {
        if (!exitConfirmed) {
            debug("finish() blocked - not confirmed")
            return
        }
        super.finish()
    }

    private fun showExitDialog() {
        try {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_confirm))
                .setPositiveButton(R.string.yes) { _, _ ->
                    exitConfirmed = true
                    finish()
                }
                .setNegativeButton(R.string.no, null)
                .show()
        } catch (e: Exception) {
            debug("Dialog error: ${e.message}")
        }
    }

    /** Interface for fragments that handle D-pad navigation */
    interface DpadNavigable {
        fun canGoLeft(): Boolean
    }
}
