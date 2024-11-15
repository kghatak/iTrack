package com.dol.itrack.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dol.itrack.db.models.Event
import com.dol.itrack.db.models.UserProfile

@Database(entities = [UserProfile::class, Event::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}