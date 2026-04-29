package com.veltrix.tv.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_index",
    indices = [Index(value = ["nameLower"])]
)
data class SearchIndexEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameLower: String,
    val icon: String?,
    val type: String,
    val streamId: Int = 0,
    val seriesId: Int = 0,
    val categoryId: String?,
    val containerExtension: String?
)
