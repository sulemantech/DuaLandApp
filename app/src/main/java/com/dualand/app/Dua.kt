package com.dualand.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class DuaFav(
   @PrimaryKey val id: Int,
    val title: String,
    val arabicText: String,
    val translation: String,
    val isFavorite: Boolean = false
)
