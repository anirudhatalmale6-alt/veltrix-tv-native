package com.veltrix.tv.ui.multiscreen

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MultiScreenFragment : Fragment() {

    private val players = arrayOfNulls<ExoPlayer>(4)
    private val playerViews = arrayOfNulls<PlayerView>(4)
    private val channelLabels = arrayOfNulls<TextView>(4)
    private val focusBorders = arrayOfNulls<View>(4)
    private val containers = arrayOfNulls<View>(4)

    private var allChannels = listOf<LiveStream>()
    private var channelNames = arrayOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_multiscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViews[0] = view.findViewById(R.id.playerView1)
        playerViews[1] = view.findViewById(R.id.playerView2)
        playerViews[2] = view.findViewById(R.id.playerView3)
        playerViews[3] = view.findViewById(R.id.playerView4)

        channelLabels[0] = view.findViewById(R.id.tvChannel1)
        channelLabels[1] = view.findViewById(R.id.tvChannel2)
        channelLabels[2] = view.findViewById(R.id.tvChannel3)
        channelLabels[3] = view.findViewById(R.id.tvChannel4)

        focusBorders[0] = view.findViewById(R.id.focusBorder1)
        focusBorders[1] = view.findViewById(R.id.focusBorder2)
        focusBorders[2] = view.findViewById(R.id.focusBorder3)
        focusBorders[3] = view.findViewById(R.id.focusBorder4)

        containers[0] = view.findViewById(R.id.player1Container)
        containers[1] = view.findViewById(R.id.player2Container)
        containers[2] = view.findViewById(R.id.player3Container)
        containers[3] = view.findViewById(R.id.player4Container)

        // Setup focus highlight and click handlers
        for (i in 0..3) {
            val idx = i
            focusBorders[i]?.setOnFocusChangeListener { _, hasFocus ->
                (containers[idx] as? ViewGroup)?.let { container ->
                    if (hasFocus) {
                        container.setBackgroundColor(Color.parseColor("#44FF0000"))
                    } else {
                        container.setBackgroundColor(Color.parseColor("#1A1A2E"))
                    }
                }
            }
            focusBorders[i]?.setOnClickListener {
                showChannelPicker(idx)
            }
        }

        // Focus the first quadrant
        focusBorders[0]?.requestFocus()

        // Load available channels
        loadChannels()
    }

    private fun loadChannels() {
        val prefs = MainActivity.prefsInstance
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getLiveCategories(prefs.username, prefs.password)
                }
                // Load channels from first few categories to keep it fast
                val channels = mutableListOf<LiveStream>()
                for (cat in categories.take(20)) {
                    if (channels.size >= 500) break
                    try {
                        val streams = withContext(Dispatchers.IO) {
                            MainActivity.apiService.getLiveStreams(
                                prefs.username, prefs.password,
                                categoryId = cat.categoryId
                            )
                        }
                        channels.addAll(streams)
                    } catch (_: Exception) {}
                }
                allChannels = channels
                channelNames = channels.map { it.name }.toTypedArray()
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "MultiScreen loadChannels error", e)
            }
        }
    }

    private fun showChannelPicker(quadrant: Int) {
        if (channelNames.isEmpty()) {
            android.widget.Toast.makeText(requireContext(), "Loading channels...", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Select Channel for Screen ${quadrant + 1}")
            .setItems(channelNames) { _, which ->
                val stream = allChannels[which]
                playInQuadrant(quadrant, stream)
            }
            .setNegativeButton("Stop") { _, _ ->
                stopQuadrant(quadrant)
            }
            .show()
    }

    private fun playInQuadrant(quadrant: Int, stream: LiveStream) {
        // Release existing player in this quadrant
        players[quadrant]?.release()

        val prefs = MainActivity.prefsInstance
        val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${stream.streamId}.ts"

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(30_000, 60_000, 3_000, 5_000)
            .build()

        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(30_000)
            .setReadTimeoutMs(60_000)
            .setAllowCrossProtocolRedirects(true)
            .setUserAgent("VeltrixTV/1.0 (Android TV; ExoPlayer)")

        val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

        val player = ExoPlayer.Builder(requireContext())
            .setLoadControl(loadControl)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        player.setWakeMode(C.WAKE_MODE_NETWORK)

        // Auto-reconnect on error
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                android.util.Log.e("VeltrixTV", "MultiScreen Q${quadrant + 1} error", error)
                view?.postDelayed({
                    if (isAdded) {
                        player.clearMediaItems()
                        player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
                        player.prepare()
                        player.playWhenReady = true
                    }
                }, 3000)
            }
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED && isAdded) {
                    view?.postDelayed({
                        if (isAdded) {
                            player.clearMediaItems()
                            player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
                            player.prepare()
                            player.playWhenReady = true
                        }
                    }, 1000)
                }
            }
        })

        playerViews[quadrant]?.player = player
        player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
        player.prepare()
        player.playWhenReady = true
        // Mute all except focused quadrant
        player.volume = 0f

        players[quadrant] = player
        channelLabels[quadrant]?.text = "${quadrant + 1} - ${stream.name}"
    }

    private fun stopQuadrant(quadrant: Int) {
        players[quadrant]?.release()
        players[quadrant] = null
        playerViews[quadrant]?.player = null
        channelLabels[quadrant]?.text = "${quadrant + 1} - Press OK to select"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        for (i in 0..3) {
            players[i]?.release()
            players[i] = null
        }
    }
}
