package com.dol.itrack.db

import androidx.room.TypeConverter
import com.dol.itrack.db.models.Mood

class Converters {
    @TypeConverter
    fun fromMood(value: Mood): String {
        return value.name
    }

    @TypeConverter
    fun toMood(value: String): Mood {
        return Mood.valueOf(value)
    }
}