package com.dualand.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dua_fav")
data class DuaFav(
    @PrimaryKey val id: Int,
    val title: String,
    val image: Int,
    val textheading: String,
    val duaNumber: String,
    var isFavorite: Boolean
)
