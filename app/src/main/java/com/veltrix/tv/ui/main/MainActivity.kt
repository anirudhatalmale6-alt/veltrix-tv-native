package com.veltrix.tv.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.veltrix.tv.R
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.api.XtreamApiService
import com.veltrix.tv.ui.favorites.FavoritesFragment
import com.veltrix.tv.ui.live.LiveFragment
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
    private var currentContentFragment: Fragment? = null

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

        initApi()
        setupSidebar()
    }

    private fun initApi() {
        val baseUrl = prefs.getBaseUrl().trimEnd('/') + "/"
        val client = OkHttpClient.Builder()
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
            switchContent(item.id)
            // After selecting, move focus to content
            contentContainer.post {
                focusFirstInContent()
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.sidebarContainer, sidebarFragment)
            .commit()

        switchContent(SidebarFragment.ID_LIVE)
    }

    private fun switchContent(sectionId: Int) {
        val fragment: Fragment = when (sectionId) {
            SidebarFragment.ID_LIVE -> LiveFragment()
            SidebarFragment.ID_MOVIES -> VodFragment()
            SidebarFragment.ID_SERIES -> SeriesFragment()
            SidebarFragment.ID_FAVORITES -> FavoritesFragment()
            SidebarFragment.ID_SETTINGS -> SettingsFragment()
            else -> LiveFragment()
        }

        currentContentFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .commit()
    }

    private fun isFocusInSidebar(): Boolean {
        val focused = currentFocus ?: return false
        return sidebarContainer.isAncestorOf(focused)
    }

    private fun isFocusInContent(): Boolean {
        val focused = currentFocus ?: return false
        return contentContainer.isAncestorOf(focused)
    }

    private fun focusFirstInContent() {
        // Find the first focusable view in the content area
        val firstFocusable = contentContainer.findFocus()
            ?: findFirstFocusable(contentContainer)
        firstFocusable?.requestFocus()
    }

    private fun findFirstFocusable(view: android.view.View): android.view.View? {
        if (view.isFocusable) return view
        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                val found = findFirstFocusable(view.getChildAt(i))
                if (found != null) return found
            }
        }
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (isFocusInSidebar()) {
                    // Move from sidebar to content area
                    focusFirstInContent()
                    return true
                }
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (isFocusInContent()) {
                    // Check if the content fragment can handle LEFT (e.g., categories list)
                    val frag = currentContentFragment
                    if (frag is DpadNavigable && frag.canGoLeft()) {
                        return false // Let the fragment handle it
                    }
                    // Move from content back to sidebar
                    sidebarFragment.focusCurrentItem()
                    return true
                }
            }
            KeyEvent.KEYCODE_BACK -> {
                if (isFocusInContent()) {
                    // BACK from content -> sidebar
                    sidebarFragment.focusCurrentItem()
                    return true
                }
                // BACK from sidebar -> exit dialog
                showExitDialog()
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this, R.style.AppTheme)
            .setTitle(getString(R.string.exit_confirm))
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun android.view.View.isAncestorOf(view: android.view.View): Boolean {
        var current: android.view.ViewParent? = view.parent
        while (current != null) {
            if (current == this) return true
            current = current.parent
        }
        return false
    }

    /** Interface for fragments that handle D-pad navigation */
    interface DpadNavigable {
        /** Returns true if this fragment has a sub-area to navigate LEFT to (e.g., categories) */
        fun canGoLeft(): Boolean
    }
}
