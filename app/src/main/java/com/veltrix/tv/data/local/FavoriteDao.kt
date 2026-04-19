package com.veltrix.tv.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY name ASC")
    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE streamId = :streamId AND type = :type)")
    suspend fun isFavorite(streamId: Int, type: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE streamId = :streamId AND type = :type")
    suspend fun delete(streamId: Int, type: String)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}
