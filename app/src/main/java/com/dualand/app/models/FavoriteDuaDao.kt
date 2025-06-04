package com.dualand.app.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDuaDao {

    @Query("SELECT * FROM duas_status")
    fun getAllDuaStatuses(): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE favorite = 1")
    fun getAllFavoriteDuas(): Flow<List<DuaStatusEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM duas_status WHERE duaId = :id AND favorite = 1)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("UPDATE duas_status SET favorite = :isFavorite WHERE duaId = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("UPDATE duas_status SET status = :newStatus WHERE duaId = :id")
    suspend fun updateDuaStatus(id: Int, newStatus: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(duaStatus: DuaStatusEntity)

    @Query("SELECT * FROM duas_status WHERE status = :status")
    fun getDuasByStatus(status: String): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE status = :status AND favorite = 1")
    fun getFavoriteDuasByStatus(status: String): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE duaId = :id")
    suspend fun getDuaStatusById(id: Int): DuaStatusEntity?
}
