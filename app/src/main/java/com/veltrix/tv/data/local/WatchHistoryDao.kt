package com.veltrix.tv.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {

    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT 50")
    fun getRecentHistory(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE type = :type ORDER BY watchedAt DESC LIMIT 50")
    fun getHistoryByType(type: String): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE streamId = :streamId AND type = :type ORDER BY watchedAt DESC LIMIT 1")
    suspend fun getLastWatched(streamId: Int, type: String): WatchHistoryEntity?

    @Query("SELECT * FROM watch_history WHERE seriesId = :seriesId ORDER BY watchedAt DESC LIMIT 1")
    suspend fun getLastWatchedEpisode(seriesId: Int): WatchHistoryEntity?

    @Query("SELECT * FROM watch_history WHERE seriesId = :seriesId AND seasonNumber = :season AND episodeNumber = :episode LIMIT 1")
    suspend fun getEpisodeProgress(seriesId: Int, season: String, episode: Int): WatchHistoryEntity?

    @Query("SELECT * FROM watch_history WHERE seriesId = :seriesId ORDER BY watchedAt DESC")
    suspend fun getSeriesHistory(seriesId: Int): List<WatchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WatchHistoryEntity): Long

    @Query("DELETE FROM watch_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM watch_history")
    suspend fun deleteAll()

    @Query("UPDATE watch_history SET positionMs = :positionMs, durationMs = :durationMs, watchedAt = :watchedAt WHERE id = :id")
    suspend fun updateProgress(id: Long, positionMs: Long, durationMs: Long, watchedAt: Long)
}
