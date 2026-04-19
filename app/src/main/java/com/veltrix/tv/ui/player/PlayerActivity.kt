package com.veltrix.tv.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.veltrix.tv.R
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_CHANNEL_NAME = "channel_name"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val EXTRA_STREAM_TYPE = "stream_type"
        const val EXTRA_STREAM_IDS = "stream_ids"
        const val EXTRA_STREAM_NAMES = "stream_names"
        const val EXTRA_CURRENT_INDEX = "current_index"
        private const val OVERLAY_HIDE_DELAY = 3000L
    }

    private lateinit var playerView: PlayerView
    private lateinit var overlayInfo: LinearLayout
    private lateinit var tvChannelName: TextView
    private lateinit var tvChannelCategory: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    private var player: ExoPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private var streamUrl: String = ""
    private var channelName: String = ""
    private var categoryName: String = ""
    private var streamType: String = "live"

    // For channel zapping (live only)
    private var streamIds: IntArray? = null
    private var streamNames: Array<String>? = null
    private var currentIndex: Int = 0

    private var isOverlayVisible = false

    private val hideOverlayRunnable = Runnable {
        hideOverlay()
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

        streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: ""
        channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: ""
        streamType = intent.getStringExtra(EXTRA_STREAM_TYPE) ?: "live"
        streamIds = intent.getIntArrayExtra(EXTRA_STREAM_IDS)
        streamNames = intent.getStringArrayExtra(EXTRA_STREAM_NAMES)
        currentIndex = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0)

        tvChannelName.text = channelName
        tvChannelCategory.text = categoryName

        initPlayer()
        playStream(streamUrl)
        showOverlay()
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build().also {
            playerView.player = it

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
                        }
                        Player.STATE_ENDED -> {
                            if (streamType == "live") {
                                // Re-try live stream
                                playStream(streamUrl)
                            } else {
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
    }

    private fun playStream(url: String) {
        player?.let {
            progressBar.visible()
            tvError.gone()
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            it.setMediaItem(mediaItem)
            it.prepare()
            it.playWhenReady = true
        }
    }

    private fun showOverlay() {
        overlayInfo.visible()
        isOverlayVisible = true
        handler.removeCallbacks(hideOverlayRunnable)
        handler.postDelayed(hideOverlayRunnable, OVERLAY_HIDE_DELAY)
    }

    private fun hideOverlay() {
        overlayInfo.gone()
        isOverlayVisible = false
    }

    private fun toggleOverlay() {
        if (isOverlayVisible) hideOverlay() else showOverlay()
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
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER, KeyEvent.KEYCODE_SPACE -> {
                if (streamType != "live") {
                    // For VOD/series: toggle play/pause
                    player?.let {
                        it.playWhenReady = !it.playWhenReady
                    }
                }
                toggleOverlay()
                true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                player?.let {
                    it.playWhenReady = !it.playWhenReady
                }
                showOverlay()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (streamType == "live") {
                    zapChannel(-1)
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (streamType == "live") {
                    zapChannel(1)
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (streamType != "live") {
                    // Seek backward 10 seconds for VOD/series
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
                    // Seek forward 10 seconds for VOD/series
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
                finish()
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (isOverlayVisible) {
                    hideOverlay()
                    true
                } else {
                    finish()
                    true
                }
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
    }
}
