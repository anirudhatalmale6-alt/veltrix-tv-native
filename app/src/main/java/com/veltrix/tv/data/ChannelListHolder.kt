package com.veltrix.tv.data

import com.veltrix.tv.data.models.LiveStream

/**
 * Holds the current channel list in memory to avoid passing large arrays
 * through Intent extras (which can exceed Android's 1MB Binder transaction limit).
 */
object ChannelListHolder {
    var streamIds: IntArray = intArrayOf()
    var streamNames: Array<String> = arrayOf()
    var streams: List<LiveStream> = emptyList()

    fun set(liveStreams: List<LiveStream>) {
        streams = liveStreams
        streamIds = liveStreams.map { it.streamId }.toIntArray()
        streamNames = liveStreams.map { it.name }.toTypedArray()
    }

    fun clear() {
        streams = emptyList()
        streamIds = intArrayOf()
        streamNames = arrayOf()
    }
}
