package com.veltrix.tv.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val streamId: Int,
    val name: String,
    val icon: String?,
    val type: String, // "live", "vod", "series"
    val categoryId: String?,
    val containerExtension: String?,
    val seriesId: Int? = null
)
