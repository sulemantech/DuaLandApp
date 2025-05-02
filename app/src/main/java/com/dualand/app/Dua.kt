package com.dualand.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class Dua(
    @PrimaryKey val id: Int,
    val title: String,
    val arabic: String,
    val isFavorite: Boolean = false
)
