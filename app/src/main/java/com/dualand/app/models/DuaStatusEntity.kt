package com.dualand.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas_status")
data class DuaStatusEntity(
    @PrimaryKey val duaNumber: String,
    val favorite: Boolean = false,
    val status: String  // "Memorized", "In Practice", "Not Started"
)
