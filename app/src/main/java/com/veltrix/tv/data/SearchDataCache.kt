package com.veltrix.tv.data

import com.veltrix.tv.data.local.SearchIndexEntity
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.data.models.SeriesItem

object SearchDataCache {
    @Volatile var isLoaded = false
    @Volatile var isLoading = false

    var liveCount = 0
    var vodCount = 0
    var seriesCount = 0

    var liveStreams: List<LiveStream> = emptyList()
    var vodStreams: List<VodStream> = emptyList()
    var seriesItems: List<SeriesItem> = emptyList()

    fun toLiveEntities(streams: List<LiveStream>): List<SearchIndexEntity> {
        return streams.map {
            SearchIndexEntity(
                name = it.name,
                nameLower = it.name.lowercase(),
                icon = it.streamIcon,
                type = "live",
                streamId = it.streamId,
                categoryId = it.categoryId,
                containerExtension = null
            )
        }
    }

    fun toVodEntities(streams: List<VodStream>): List<SearchIndexEntity> {
        return streams.map {
            SearchIndexEntity(
                name = it.name,
                nameLower = it.name.lowercase(),
                icon = it.streamIcon,
                type = "vod",
                streamId = it.streamId,
                categoryId = it.categoryId,
                containerExtension = it.containerExtension
            )
        }
    }

    fun toSeriesEntities(items: List<SeriesItem>): List<SearchIndexEntity> {
        return items.map {
            SearchIndexEntity(
                name = it.name,
                nameLower = it.name.lowercase(),
                icon = it.cover,
                type = "series",
                seriesId = it.seriesId,
                categoryId = it.categoryId,
                containerExtension = null
            )
        }
    }
}
