package com.dualand.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dualand.app.models.FavoriteDuaDao
import com.dualand.app.models.FavoriteDuaEntity


@Database(entities = [FavoriteDuaEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDuaDao(): FavoriteDuaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dua_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}

