package com.novotechx.trakkr.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getTrakkrDatabaseBuilder(context: Context): RoomDatabase.Builder<TrakkrDatabase> {
    val dbFile = context.getDatabasePath(TrakkrDatabase.DATABASE_NAME)
    return Room.databaseBuilder<TrakkrDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    )
}
