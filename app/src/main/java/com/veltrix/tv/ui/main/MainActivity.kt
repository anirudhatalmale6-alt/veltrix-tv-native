package com.veltrix.tv.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.veltrix.tv.R
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.api.XtreamApiService
import com.veltrix.tv.ui.favorites.FavoritesFragment
import com.veltrix.tv.ui.history.HistoryFragment
import com.veltrix.tv.ui.live.LiveFragment
import com.veltrix.tv.ui.multiscreen.MultiScreenFragment
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.ui.search.SearchFragment
import com.veltrix.tv.ui.series.SeriesFragment
import com.veltrix.tv.ui.settings.SettingsFragment
import com.veltrix.tv.ui.vod.VodFragment
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var sidebarFragment: SidebarFragment
    private lateinit var prefs: PrefsManager
    private lateinit var contentContainer: FrameLayout
    private lateinit var sidebarContainer: FrameLayout
    private lateinit var tvDebug: TextView
    private lateinit var miniPlayerContainer: FrameLayout
    private lateinit var miniPlayerView: PlayerView
    private lateinit var tvMiniChannelName: TextView
    private lateinit var btnCloseMiniPlayer: ImageButton
    private var miniPlayer: ExoPlayer? = null
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
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

        miniPlayerContainer = findViewById(R.id.miniPlayerContainer)
        miniPlayerView = findViewById(R.id.miniPlayerView)
        tvMiniChannelName = findViewById(R.id.tvMiniChannelName)
        btnCloseMiniPlayer = findViewById(R.id.btnCloseMiniPlayer)

        btnCloseMiniPlayer.setOnClickListener {
            closeMiniPlayer()
        }

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
        // Clear any old HTTP cache that may serve stale responses
        try {
            val oldCacheDir = java.io.File(cacheDir, "http_cache")
            if (oldCacheDir.exists()) oldCacheDir.deleteRecursively()
        } catch (_: Exception) {}
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                // Force fresh requests - no caching for IPTV API
                val request = chain.request().newBuilder()
                    .header("Cache-Control", "no-cache, no-store")
                    .build()
                chain.proceed(request)
            }
            .build()

        val gson = GsonBuilder().setLenient().create()
        apiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
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
                SidebarFragment.ID_MULTISCREEN -> MultiScreenFragment()
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
                    // Close mini-player first if visible
                    if (isMiniPlayerVisible()) {
                        closeMiniPlayer()
                        return true
                    }
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

    // Mini player methods
    private var miniPlayerUrl: String = ""

    fun startMiniPlayer(streamUrl: String, channelName: String) {
        try {
            closeMiniPlayer()
            miniPlayerUrl = streamUrl

            // Use larger buffer and long timeouts for mini-player too
            val loadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(60_000, 120_000, 5_000, 10_000)
                .build()
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setConnectTimeoutMs(30_000)
                .setReadTimeoutMs(60_000)
                .setAllowCrossProtocolRedirects(true)
                .setUserAgent("VeltrixTV/1.0 (Android TV; ExoPlayer)")
            val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

            miniPlayer = ExoPlayer.Builder(this)
                .setLoadControl(loadControl)
                .setMediaSourceFactory(mediaSourceFactory)
                .build().also {
                miniPlayerView.player = it
                it.setWakeMode(C.WAKE_MODE_NETWORK)
                val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))
                it.setMediaItem(mediaItem)
                it.prepare()
                it.playWhenReady = true

                // Auto-reconnect mini-player on errors
                it.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        debug("Mini player error, reconnecting: ${error.errorCodeName}")
                        handler.postDelayed({
                            miniPlayer?.let { mp ->
                                mp.clearMediaItems()
                                mp.setMediaItem(MediaItem.fromUri(Uri.parse(miniPlayerUrl)))
                                mp.prepare()
                                mp.playWhenReady = true
                            }
                        }, 3000)
                    }
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            // Live stream ended, reconnect
                            handler.postDelayed({
                                miniPlayer?.let { mp ->
                                    mp.clearMediaItems()
                                    mp.setMediaItem(MediaItem.fromUri(Uri.parse(miniPlayerUrl)))
                                    mp.prepare()
                                    mp.playWhenReady = true
                                }
                            }, 1000)
                        }
                    }
                })
            }
            tvMiniChannelName.text = channelName
            miniPlayerContainer.visibility = android.view.View.VISIBLE
        } catch (e: Exception) {
            debug("Mini player error: ${e.message}")
        }
    }

    fun closeMiniPlayer() {
        try {
            miniPlayer?.release()
            miniPlayer = null
            miniPlayerContainer.visibility = android.view.View.GONE
        } catch (_: Exception) {}
    }

    fun isMiniPlayerVisible(): Boolean {
        return miniPlayerContainer.visibility == android.view.View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // Check if PlayerActivity requested a mini-player
        val url = PlayerActivity.pendingMiniPlayerUrl
        val name = PlayerActivity.pendingMiniPlayerName
        if (url != null && name != null) {
            PlayerActivity.pendingMiniPlayerUrl = null
            PlayerActivity.pendingMiniPlayerName = null
            startMiniPlayer(url, name)
        }
    }

    override fun onPause() {
        super.onPause()
        // When leaving to PlayerActivity, close mini-player to prevent mixed audio
        if (PlayerActivity.shouldCloseMiniPlayer) {
            PlayerActivity.shouldCloseMiniPlayer = false
            closeMiniPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeMiniPlayer()
    }

    /** Interface for fragments that handle D-pad navigation */
    interface DpadNavigable {
        fun canGoLeft(): Boolean
    }
}
