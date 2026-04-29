package com.veltrix.tv.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.veltrix.tv.R
import com.veltrix.tv.data.PrefsManager
import com.veltrix.tv.data.SearchDataCache
import com.veltrix.tv.data.local.AppDatabase
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.InetAddress
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
        const val USER_AGENT = "Lavf/60.3.100"
        lateinit var apiService: XtreamApiService
            private set
        lateinit var prefsInstance: PrefsManager
            private set

        // DNS-over-HTTPS resolvers for WiFi networks where ISP DNS blocks IPTV domains
        private val bootstrapClient by lazy { OkHttpClient.Builder().build() }
        private val googleDoH by lazy {
            DnsOverHttps.Builder()
                .client(bootstrapClient)
                .url("https://dns.google/dns-query".toHttpUrl())
                .bootstrapDnsHosts(
                    InetAddress.getByName("8.8.8.8"),
                    InetAddress.getByName("8.8.4.4")
                )
                .build()
        }
        private val cloudflareDoH by lazy {
            DnsOverHttps.Builder()
                .client(bootstrapClient)
                .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
                .bootstrapDnsHosts(
                    InetAddress.getByName("1.1.1.1"),
                    InetAddress.getByName("1.0.0.1")
                )
                .build()
        }

        private fun preferIPv4(addresses: List<InetAddress>): List<InetAddress> {
            val ipv4 = addresses.filter { it is Inet4Address }
            return if (ipv4.isNotEmpty()) ipv4 else addresses
        }

        val IPTV_DNS = object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                // Try system DNS first
                try {
                    val systemResult = Dns.SYSTEM.lookup(hostname)
                    if (systemResult.isNotEmpty()) return preferIPv4(systemResult)
                } catch (_: Exception) {}

                // WiFi DNS failed - try Google DNS-over-HTTPS
                try {
                    val googleResult = googleDoH.lookup(hostname)
                    if (googleResult.isNotEmpty()) return preferIPv4(googleResult)
                } catch (_: Exception) {}

                // Last resort - Cloudflare DNS-over-HTTPS
                try {
                    val cfResult = cloudflareDoH.lookup(hostname)
                    if (cfResult.isNotEmpty()) return preferIPv4(cfResult)
                } catch (_: Exception) {}

                return emptyList()
            }
        }

        fun createHttpClient(connectTimeoutSec: Long = 30, readTimeoutSec: Long = 120): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
                .dns(IPTV_DNS)
                .retryOnConnectionFailure(true)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("Cache-Control", "no-cache, no-store")
                        .header("User-Agent", USER_AGENT)
                        .header("Connection", "keep-alive")
                        .header("Accept", "*/*")
                        .build()
                    chain.proceed(request)
                }
                .build()
        }

        fun createStreamClient(connectTimeoutSec: Long = 30, readTimeoutSec: Long = 60): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
                .dns(IPTV_DNS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", USER_AGENT)
                        .header("Connection", "keep-alive")
                        .header("Accept", "*/*")
                        .header("Referer", chain.request().url.scheme + "://" + chain.request().url.host + "/")
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep screen on while app is running (prevents screen going black after idle)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

        // Search data is loaded on-demand when user opens SearchFragment
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
        val client = createHttpClient(30, 120)

        val gson = GsonBuilder().setLenient().create()
        apiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(XtreamApiService::class.java)
    }

    fun preloadSearchData() {
        if (SearchDataCache.isLoaded || SearchDataCache.isLoading) return
        SearchDataCache.isLoading = true

        val dao = AppDatabase.getInstance(this).searchIndexDao()
        val client = createHttpClient(30, 300)
        val baseUrl = prefs.getBaseUrl().trimEnd('/')

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                dao.deleteAllSync()

                SearchDataCache.liveCount = streamParseAndInsert(
                    client, baseUrl, "get_live_streams", "live", dao
                )
                SearchDataCache.vodCount = streamParseAndInsert(
                    client, baseUrl, "get_vod_streams", "vod", dao
                )
                SearchDataCache.seriesCount = streamParseAndInsert(
                    client, baseUrl, "get_series", "series", dao
                )

                val total = dao.countSync()
                if (total > 0) SearchDataCache.isLoaded = true
                SearchDataCache.isLoading = false
                debug("Search DB: $total items (${SearchDataCache.liveCount} live, ${SearchDataCache.vodCount} vod, ${SearchDataCache.seriesCount} series)")
            } catch (e: OutOfMemoryError) {
                SearchDataCache.isLoading = false
                debug("Search preload OOM")
            } catch (e: Exception) {
                SearchDataCache.isLoading = false
                debug("Search preload error: ${e.message}")
            }
        }
    }

    fun streamParseAndInsert(
        client: OkHttpClient,
        baseUrl: String,
        action: String,
        type: String,
        dao: com.veltrix.tv.data.local.SearchIndexDao
    ): Int {
        val url = "$baseUrl/player_api.php?username=${prefs.username}&password=${prefs.password}&action=$action"
        val request = okhttp3.Request.Builder()
            .url(url)
            .header("User-Agent", USER_AGENT)
            .header("Cache-Control", "no-cache")
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            response.close()
            return 0
        }

        val body = response.body ?: return 0
        var count = 0
        val batch = mutableListOf<com.veltrix.tv.data.local.SearchIndexEntity>()
        val batchSize = 500

        try {
            val reader = com.google.gson.stream.JsonReader(body.charStream())
            reader.isLenient = true
            reader.beginArray()

            while (reader.hasNext()) {
                var name = ""
                var streamId = 0
                var seriesId = 0
                var icon: String? = null
                var categoryId: String? = null
                var containerExt: String? = null

                reader.beginObject()
                while (reader.hasNext()) {
                    val key = reader.nextName()
                    if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                        reader.skipValue()
                        continue
                    }
                    try {
                        when (key) {
                            "name" -> name = reader.nextString() ?: ""
                            "stream_id" -> {
                                val v = reader.nextString()
                                streamId = v?.toIntOrNull() ?: 0
                            }
                            "series_id" -> {
                                val v = reader.nextString()
                                seriesId = v?.toIntOrNull() ?: 0
                            }
                            "stream_icon", "cover" -> icon = reader.nextString()
                            "category_id" -> categoryId = reader.nextString()
                            "container_extension" -> containerExt = reader.nextString()
                            else -> reader.skipValue()
                        }
                    } catch (_: Exception) {
                        try { reader.skipValue() } catch (_: Exception) {}
                    }
                }
                reader.endObject()

                if (name.isNotEmpty()) {
                    batch.add(
                        com.veltrix.tv.data.local.SearchIndexEntity(
                            name = name,
                            nameLower = name.lowercase(),
                            icon = icon,
                            type = type,
                            streamId = streamId,
                            seriesId = seriesId,
                            categoryId = categoryId,
                            containerExtension = containerExt
                        )
                    )
                    count++

                    if (batch.size >= batchSize) {
                        dao.insertAllSync(batch.toList())
                        batch.clear()
                    }
                }
            }

            reader.endArray()
            reader.close()

            if (batch.isNotEmpty()) {
                dao.insertAllSync(batch.toList())
                batch.clear()
            }
        } catch (_: Exception) {
            if (batch.isNotEmpty()) {
                try { dao.insertAllSync(batch.toList()) } catch (_: Exception) {}
                batch.clear()
            }
        } finally {
            body.close()
        }

        return count
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
                // Ask fragment for its main content view (e.g. channel list, movie grid)
                val frag = currentContentFragment
                if (frag is DpadNavigable) {
                    val mainView = frag.getMainContentView()
                    if (mainView is RecyclerView && mainView.childCount > 0) {
                        mainView.getChildAt(0)?.requestFocus()
                        return@post
                    }
                }
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
                            return true
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
                    exitApp()
                }
                .setNegativeButton(R.string.no, null)
                .show()
        } catch (e: Exception) {
            debug("Dialog error: ${e.message}")
        }
    }

    private fun exitApp() {
        try {
            handler.removeCallbacksAndMessages(null)
            com.veltrix.tv.util.DashboardTracker.stop()
            closeMiniPlayer()
            SearchDataCache.liveStreams = emptyList()
            SearchDataCache.vodStreams = emptyList()
            SearchDataCache.seriesItems = emptyList()
            SearchDataCache.isLoaded = false
            SearchDataCache.isLoading = false
            exitConfirmed = true
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (_: Exception) {
            exitConfirmed = true
            finish()
        }
    }

    // Mini player methods
    private var miniPlayerUrl: String = ""

    fun startMiniPlayer(streamUrl: String, channelName: String) {
        try {
            closeMiniPlayer()
            miniPlayerUrl = streamUrl

            val loadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(10_000, 20_000, 2_000, 5_000)
                .build()
            val streamClient = createStreamClient(30, 60)
            val httpDataSourceFactory = OkHttpDataSource.Factory(streamClient)
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
            handler.removeCallbacksAndMessages(null)
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
        handler.removeCallbacksAndMessages(null)
        closeMiniPlayer()
        super.onDestroy()
    }

    /** Interface for fragments that handle D-pad navigation */
    interface DpadNavigable {
        fun canGoLeft(): Boolean
        fun getMainContentView(): View? = null
    }
}
