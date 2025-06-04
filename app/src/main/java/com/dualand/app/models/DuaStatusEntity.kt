package com.dualand.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas_status")
data class DuaStatusEntity(
    @PrimaryKey val duaId: Int,
    val favorite: Boolean,
    val status: String = "Not Started"
)

