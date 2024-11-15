package com.dol.itrack.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dol.itrack.db.models.Event
import com.dol.itrack.db.models.UserProfile

@Dao
interface AppDao {
    @Insert
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Insert
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM UserProfile WHERE username = :username")
    suspend fun getUserProfile(username: String): UserProfile?

    @Query("SELECT * FROM Event WHERE timestamp = :timestamp")
    suspend fun getEvent(timestamp: Long): Event?
}