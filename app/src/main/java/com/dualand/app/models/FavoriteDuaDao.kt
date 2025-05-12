package com.dualand.app.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDuaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(dua: DuaStatusEntity)

    @Delete
    suspend fun removeFavorite(dua: DuaStatusEntity)

    @Query("SELECT * FROM duas_status WHERE favorite = 1")
    fun getAllFavoriteDuas(): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status")
    fun getAllDuaStatuses(): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status")
    fun getAllDuas(): Flow<List<DuaStatusEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM duas_status WHERE duaNumber = :duaNumber AND favorite = 1)")
    suspend fun isFavorite(duaNumber: String): Boolean

    @Query("UPDATE duas_status SET favorite = :isFav WHERE duaNumber = :duaNumber")
    suspend fun updateFavoriteStatus(duaNumber: String, isFav: Boolean)

    @Query("UPDATE duas_status SET status = :newStatus WHERE duaNumber = :duaNumber")
    suspend fun updateDuaStatus(duaNumber: String, newStatus: String)

    @Query("SELECT * FROM duas_status WHERE status = :status")
    fun getDuasByStatus(status: String): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE favorite = 1 AND status = :status")
    fun getFavoriteDuasByStatus(status: String): Flow<List<DuaStatusEntity>>
}
