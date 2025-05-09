package com.dualand.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_duas")
data class FavoriteDuaEntity(
    @PrimaryKey val duaNumber: String,
    val textHeading: String,
    val imageResId: Int
)
