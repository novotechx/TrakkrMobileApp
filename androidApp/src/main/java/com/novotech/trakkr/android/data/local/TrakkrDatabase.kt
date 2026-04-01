package com.novotech.trakkr.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novotech.trakkr.android.data.local.dao.ActivityDao
import com.novotech.trakkr.android.data.local.dao.RoutePointDao
import com.novotech.trakkr.android.data.local.entity.ActivityEntity
import com.novotech.trakkr.android.data.local.entity.RoutePointEntity

@Database(
    entities = [ActivityEntity::class, RoutePointEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TrakkrDatabase : RoomDatabase()
{
    abstract fun activityDao(): ActivityDao
    abstract fun routePointDao(): RoutePointDao

    companion object
    {
        const val DATABASE_NAME = "trakkr.db"
    }
}
