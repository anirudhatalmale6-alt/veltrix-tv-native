package com.veltrix.tv.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SearchIndexEntity>)

    @Query("DELETE FROM search_index")
    suspend fun deleteAll()

    @Query("SELECT * FROM search_index WHERE nameLower LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchAll(query: String, limit: Int = 150): List<SearchIndexEntity>

    @Query("SELECT * FROM search_index WHERE type = :type AND nameLower LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchByType(type: String, query: String, limit: Int = 50): List<SearchIndexEntity>

    @Query("SELECT COUNT(*) FROM search_index")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM search_index WHERE type = :type")
    suspend fun countByType(type: String): Int
}
