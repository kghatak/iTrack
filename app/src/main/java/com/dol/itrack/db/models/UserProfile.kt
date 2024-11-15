package com.dol.itrack.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserProfile")
data class UserProfile(
    @PrimaryKey val timestamp: Long,
    val username: String,
    val emailID: String,
    val profilePicture: String,
    val name: String,
    val gender: String,
    val age: Int,
    val appSettings: String
)