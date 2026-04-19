package com.veltrix.tv.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val streamId: Int,
    val name: String,
    val icon: String?,
    val type: String, // "live", "vod", "series"
    val categoryId: String?,
    val containerExtension: String?,
    val seriesId: Int? = null,
    val seasonNumber: String? = null,
    val episodeNumber: Int? = null,
    val episodeTitle: String? = null,
    val watchedAt: Long = System.currentTimeMillis(),
    val positionMs: Long = 0,
    val durationMs: Long = 0
)
