package com.dol.itrack.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Event")
data class Event(
    @PrimaryKey val timestamp: Long,
    val mood: Mood,
    val topOfMind: String,
    val lastHourActivity: String,
    val extra: String
)

enum class Mood {
    HAPPY, SAD, ANGRY, NEUTRAL, FRUSTRATED, FEELING_GOOD, FEELING_BAD, FEELING_AWESOME, FEELING_TERRIBLE, FEELING_LOW
}