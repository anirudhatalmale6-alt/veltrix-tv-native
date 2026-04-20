package com.veltrix.tv.data

import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.data.models.SeriesItem

/**
 * Global cache for search data. Preloaded at app startup so search is instant.
 */
object SearchDataCache {
    var liveStreams: List<LiveStream> = emptyList()
    var vodStreams: List<VodStream> = emptyList()
    var seriesItems: List<SeriesItem> = emptyList()
    var isLoaded = false
    var isLoading = false
}
