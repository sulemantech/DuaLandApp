package com.dualand.app.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDuaDao {

    @Query("SELECT * FROM duas_status")
    fun getAllDuaStatuses(): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE favorite = 1")
    fun getAllFavoriteDuas(): Flow<List<DuaStatusEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM duas_status WHERE duaNumber = :duaNumber AND favorite = 1)")
    suspend fun isFavorite(duaNumber: String): Boolean

    @Query("UPDATE duas_status SET favorite = :isFavorite WHERE duaNumber = :duaNumber")
    suspend fun updateFavoriteStatus(duaNumber: String, isFavorite: Boolean)

    @Query("UPDATE duas_status SET status = :newStatus WHERE duaNumber = :duaNumber")
    suspend fun updateDuaStatus(duaNumber: String, newStatus: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(duaStatus: DuaStatusEntity)

    @Query("SELECT * FROM duas_status WHERE status = :status")
    fun getDuasByStatus(status: String): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE status = :status AND favorite = 1")
    fun getFavoriteDuasByStatus(status: String): Flow<List<DuaStatusEntity>>

    @Query("SELECT * FROM duas_status WHERE duaNumber = :duaNumber")
    suspend fun getDuaStatusByNumber(duaNumber: String): DuaStatusEntity?

}
