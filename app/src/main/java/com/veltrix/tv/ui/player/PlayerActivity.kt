package com.veltrix.tv.ui.player

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Rational
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.veltrix.tv.R
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.WatchHistoryEntity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_CHANNEL_NAME = "channel_name"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val EXTRA_STREAM_TYPE = "stream_type"
        const val EXTRA_STREAM_IDS = "stream_ids"
        const val EXTRA_STREAM_NAMES = "stream_names"
        const val EXTRA_CURRENT_INDEX = "current_index"
        const val EXTRA_RESUME_POSITION = "resume_position"
        const val EXTRA_STREAM_ICON = "stream_icon"
        const val EXTRA_SERIES_ID = "series_id"
        const val EXTRA_SEASON_NUMBER = "season_number"
        const val EXTRA_EPISODE_NUMBER = "episode_number"
        const val EXTRA_EPISODE_TITLE = "episode_title"
        const val EXTRA_CONTAINER_EXT = "container_ext"
        private const val OVERLAY_HIDE_DELAY = 5000L
        private const val PROGRESS_UPDATE_INTERVAL = 1000L
    }

    private lateinit var playerView: PlayerView
    private lateinit var overlayInfo: LinearLayout
    private lateinit var tvChannelName: TextView
    private lateinit var tvChannelCategory: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    // On-screen controls
    private lateinit var controlsOverlay: LinearLayout
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnRewind: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnSubtitles: ImageButton
    private lateinit var btnPip: ImageButton
    private lateinit var tvPosition: TextView
    private lateinit var tvDuration: TextView
    private lateinit var seekBar: SeekBar

    private var player: ExoPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private var streamUrl: String = ""
    private var channelName: String = ""
    private var categoryName: String = ""
    private var streamType: String = "live"
    private var resumePosition: Long = 0

    // Watch history
    private var streamIcon: String? = null
    private var seriesId: Int? = null
    private var seasonNumber: String? = null
    private var episodeNumber: Int? = null
    private var episodeTitle: String? = null
    private var containerExt: String? = null
    private var historyId: Long = 0

    // For channel zapping (live only)
    private var streamIds: IntArray? = null
    private var streamNames: Array<String>? = null
    private var currentIndex: Int = 0

    private var isOverlayVisible = false
    private var isControlsVisible = false
    private var isInPipMode = false

    private val hideOverlayRunnable = Runnable {
        hideOverlay()
    }

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateProgressUI()
            handler.postDelayed(this, PROGRESS_UPDATE_INTERVAL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        overlayInfo = findViewById(R.id.overlayInfo)
        tvChannelName = findViewById(R.id.tvChannelName)
        tvChannelCategory = findViewById(R.id.tvChannelCategory)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        // On-screen controls
        controlsOverlay = findViewById(R.id.controlsOverlay)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnRewind = findViewById(R.id.btnRewind)
        btnForward = findViewById(R.id.btnForward)
        btnSubtitles = findViewById(R.id.btnSubtitles)
        btnPip = findViewById(R.id.btnPip)
        tvPosition = findViewById(R.id.tvPosition)
        tvDuration = findViewById(R.id.tvDuration)
        seekBar = findViewById(R.id.seekBar)

        streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: ""
        channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: ""
        streamType = intent.getStringExtra(EXTRA_STREAM_TYPE) ?: "live"
        streamIds = intent.getIntArrayExtra(EXTRA_STREAM_IDS)
        streamNames = intent.getStringArrayExtra(EXTRA_STREAM_NAMES)
        currentIndex = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0)
        resumePosition = intent.getLongExtra(EXTRA_RESUME_POSITION, 0)

        // Watch history extras
        streamIcon = intent.getStringExtra(EXTRA_STREAM_ICON)
        seriesId = intent.getIntExtra(EXTRA_SERIES_ID, 0).let { if (it == 0) null else it }
        seasonNumber = intent.getStringExtra(EXTRA_SEASON_NUMBER)
        episodeNumber = intent.getIntExtra(EXTRA_EPISODE_NUMBER, 0).let { if (it == 0) null else it }
        episodeTitle = intent.getStringExtra(EXTRA_EPISODE_TITLE)
        containerExt = intent.getStringExtra(EXTRA_CONTAINER_EXT)

        tvChannelName.text = channelName
        tvChannelCategory.text = categoryName

        setupControls()

        // Hide seek controls for live TV
        if (streamType == "live") {
            btnRewind.gone()
            btnForward.gone()
            seekBar.gone()
            tvPosition.gone()
            tvDuration.gone()
        }

        // Hide PiP button on old Android versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            btnPip.gone()
        }

        if (streamUrl.isEmpty()) {
            tvError.visible()
            tvError.text = "No stream URL provided"
            return
        }

        initPlayer()
        playStream(streamUrl)
        showOverlay()
        recordWatchHistory()
    }

    private fun setupControls() {
        btnPlayPause.setOnClickListener {
            player?.let { p ->
                p.playWhenReady = !p.playWhenReady
                updatePlayPauseIcon()
            }
            resetOverlayTimer()
        }

        btnRewind.setOnClickListener {
            player?.let {
                val pos = (it.currentPosition - 10000).coerceAtLeast(0)
                it.seekTo(pos)
            }
            resetOverlayTimer()
        }

        btnForward.setOnClickListener {
            player?.let {
                val pos = (it.currentPosition + 10000).coerceAtMost(it.duration)
                it.seekTo(pos)
            }
            resetOverlayTimer()
        }

        btnSubtitles.setOnClickListener {
            showSubtitleDialog()
            resetOverlayTimer()
        }

        btnPip.setOnClickListener {
            enterPipMode()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.let {
                        val pos = (progress.toLong() * it.duration) / 1000
                        it.seekTo(pos)
                    }
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {
                resetOverlayTimer()
            }
        })

        // Make buttons focusable for D-pad
        listOf(btnPlayPause, btnRewind, btnForward, btnSubtitles, btnPip).forEach { btn ->
            btn.isFocusable = true
            btn.isFocusableInTouchMode = true
        }
    }

    private fun updatePlayPauseIcon() {
        val isPlaying = player?.playWhenReady == true
        btnPlayPause.setImageResource(
            if (isPlaying) android.R.drawable.ic_media_pause
            else android.R.drawable.ic_media_play
        )
    }

    private fun showSubtitleDialog() {
        val p = player ?: return
        val tracks = p.currentTracks

        val textGroups = tracks.groups.filter { group ->
            group.type == C.TRACK_TYPE_TEXT
        }

        if (textGroups.isEmpty()) {
            tvError.text = "No subtitles available"
            tvError.visible()
            handler.postDelayed({ tvError.gone() }, 2000)
            return
        }

        val options = mutableListOf("Off")
        val trackInfos = mutableListOf<Pair<Int, Int>>() // group index, track index

        textGroups.forEachIndexed { groupIdx, group ->
            for (trackIdx in 0 until group.length) {
                val format = group.getTrackFormat(trackIdx)
                val label = format.label ?: format.language ?: "Track ${trackInfos.size + 1}"
                options.add(label)
                trackInfos.add(groupIdx to trackIdx)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Subtitles")
            .setItems(options.toTypedArray()) { _, which ->
                if (which == 0) {
                    // Disable subtitles
                    p.trackSelectionParameters = p.trackSelectionParameters
                        .buildUpon()
                        .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
                        .build()
                } else {
                    val (gIdx, tIdx) = trackInfos[which - 1]
                    val group = textGroups[gIdx]
                    val override = TrackSelectionOverride(group.mediaTrackGroup, tIdx)
                    p.trackSelectionParameters = p.trackSelectionParameters
                        .buildUpon()
                        .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                        .setOverrideForType(override)
                        .build()
                }
            }
            .show()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
            enterPictureInPictureMode(params)
        }
    }

    override fun onPictureInPictureModeChanged(isInPip: Boolean, config: Configuration) {
        super.onPictureInPictureModeChanged(isInPip, config)
        isInPipMode = isInPip
        if (isInPip) {
            hideOverlay()
            controlsOverlay.gone()
        }
    }

    private fun updateProgressUI() {
        val p = player ?: return
        if (streamType == "live") return

        val pos = p.currentPosition
        val dur = p.duration.let { if (it == C.TIME_UNSET) 0 else it }

        tvPosition.text = formatTime(pos)
        tvDuration.text = formatTime(dur)

        if (dur > 0) {
            seekBar.progress = ((pos * 1000) / dur).toInt()
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSec = ms / 1000
        val hours = totalSec / 3600
        val minutes = (totalSec % 3600) / 60
        val seconds = totalSec % 60
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    private fun recordWatchHistory() {
        val streamId = extractStreamId() ?: return
        val dao = AppDatabase.getInstance(this).watchHistoryDao()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val entry = WatchHistoryEntity(
                    streamId = streamId,
                    name = channelName,
                    icon = streamIcon,
                    type = streamType,
                    categoryId = null,
                    containerExtension = containerExt,
                    seriesId = seriesId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    episodeTitle = episodeTitle
                )
                historyId = dao.insert(entry)
            }
        }
    }

    private fun extractStreamId(): Int? {
        // Extract stream ID from URL
        return try {
            val parts = streamUrl.split("/")
            val last = parts.last() // e.g., "12345.ts" or "12345.mp4"
            last.substringBefore(".").toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }

    private fun saveWatchProgress() {
        if (historyId <= 0) return
        val p = player ?: return
        val pos = p.currentPosition
        val dur = p.duration.let { if (it == C.TIME_UNSET) 0 else it }

        if (pos > 0 && streamType != "live") {
            val dao = AppDatabase.getInstance(this).watchHistoryDao()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    dao.updateProgress(historyId, pos, dur, System.currentTimeMillis())
                }
            }
        }
    }

    private fun initPlayer() {
        try {
            player = ExoPlayer.Builder(this).build().also {
                playerView.player = it

                // Enable subtitle rendering
                it.trackSelectionParameters = it.trackSelectionParameters
                    .buildUpon()
                    .setPreferredTextLanguage("en")
                    .build()

                it.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                progressBar.visible()
                                tvError.gone()
                            }
                            Player.STATE_READY -> {
                                progressBar.gone()
                                tvError.gone()
                                updatePlayPauseIcon()
                                if (resumePosition > 0) {
                                    it.seekTo(resumePosition)
                                    resumePosition = 0
                                }
                            }
                            Player.STATE_ENDED -> {
                                if (streamType == "live") {
                                    playStream(streamUrl)
                                } else {
                                    saveWatchProgress()
                                    finish()
                                }
                            }
                            Player.STATE_IDLE -> {}
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        progressBar.gone()
                        tvError.visible()
                        tvError.text = getString(R.string.player_error)
                    }
                })
            }
        } catch (e: Exception) {
            android.util.Log.e("VeltrixTV", "initPlayer error", e)
            tvError.visible()
            tvError.text = "Player error: ${e.message}"
        }
    }

    private fun playStream(url: String) {
        player?.let {
            progressBar.visible()
            tvError.gone()
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.parse(url))
                .build()
            it.setMediaItem(mediaItem)
            it.prepare()
            it.playWhenReady = true
            handler.post(updateProgressRunnable)
        }
    }

    private fun showOverlay() {
        overlayInfo.visible()
        controlsOverlay.visible()
        isOverlayVisible = true
        isControlsVisible = true
        resetOverlayTimer()
    }

    private fun hideOverlay() {
        overlayInfo.gone()
        controlsOverlay.gone()
        isOverlayVisible = false
        isControlsVisible = false
    }

    private fun toggleOverlay() {
        if (isOverlayVisible) hideOverlay() else showOverlay()
    }

    private fun resetOverlayTimer() {
        handler.removeCallbacks(hideOverlayRunnable)
        handler.postDelayed(hideOverlayRunnable, OVERLAY_HIDE_DELAY)
    }

    private fun zapChannel(direction: Int) {
        val ids = streamIds ?: return
        val names = streamNames ?: return
        if (ids.isEmpty()) return

        currentIndex += direction
        if (currentIndex < 0) currentIndex = ids.size - 1
        if (currentIndex >= ids.size) currentIndex = 0

        val prefs = MainActivity.prefsInstance
        val newStreamId = ids[currentIndex]
        channelName = names[currentIndex]
        streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/$newStreamId.ts"

        tvChannelName.text = channelName
        showOverlay()
        playStream(streamUrl)
        recordWatchHistory()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER, KeyEvent.KEYCODE_SPACE -> {
                if (!isOverlayVisible) {
                    showOverlay()
                } else {
                    if (streamType != "live") {
                        player?.let {
                            it.playWhenReady = !it.playWhenReady
                            updatePlayPauseIcon()
                        }
                    }
                    resetOverlayTimer()
                }
                true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                player?.let {
                    it.playWhenReady = !it.playWhenReady
                    updatePlayPauseIcon()
                }
                showOverlay()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (streamType == "live") {
                    zapChannel(-1)
                    true
                } else if (isControlsVisible) {
                    // Let focus navigate within controls
                    super.onKeyDown(keyCode, event)
                } else {
                    showOverlay()
                    true
                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (streamType == "live") {
                    zapChannel(1)
                    true
                } else if (isControlsVisible) {
                    super.onKeyDown(keyCode, event)
                } else {
                    showOverlay()
                    true
                }
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (streamType != "live") {
                    player?.let {
                        val pos = (it.currentPosition - 10000).coerceAtLeast(0)
                        it.seekTo(pos)
                    }
                    showOverlay()
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (streamType != "live") {
                    player?.let {
                        val pos = (it.currentPosition + 10000).coerceAtMost(it.duration)
                        it.seekTo(pos)
                    }
                    showOverlay()
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            KeyEvent.KEYCODE_MEDIA_REWIND -> {
                player?.let {
                    val pos = (it.currentPosition - 30000).coerceAtLeast(0)
                    it.seekTo(pos)
                }
                showOverlay()
                true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                player?.let {
                    val pos = (it.currentPosition + 30000).coerceAtMost(it.duration)
                    it.seekTo(pos)
                }
                showOverlay()
                true
            }
            KeyEvent.KEYCODE_MEDIA_STOP -> {
                saveWatchProgress()
                finish()
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (isOverlayVisible) {
                    hideOverlay()
                    true
                } else {
                    saveWatchProgress()
                    finish()
                    true
                }
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isInPipMode) {
            player?.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isInPipMode) {
            saveWatchProgress()
            player?.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
    }
}
