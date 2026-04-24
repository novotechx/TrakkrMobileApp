package com.novotechx.trakkr.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.novotechx.trakkr.data.local.dao.ActivityDao
import com.novotechx.trakkr.data.local.dao.RoutePointDao
import com.novotechx.trakkr.data.local.entity.ActivityEntity
import com.novotechx.trakkr.data.local.entity.RoutePointEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [ActivityEntity::class, RoutePointEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(TrakkrDatabaseConstructor::class)
abstract class TrakkrDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun routePointDao(): RoutePointDao

    companion object {
        const val DATABASE_NAME = "trakkr.db"
    }
}

// Room's KSP processor generates the actual implementation per platform.
@Suppress("KotlinNoActualForExpect", "EXPECTED_EXTERNAL_DECLARATION")
expect object TrakkrDatabaseConstructor : RoomDatabaseConstructor<TrakkrDatabase> {
    override fun initialize(): TrakkrDatabase
}

fun buildTrakkrDatabase(builder: RoomDatabase.Builder<TrakkrDatabase>): TrakkrDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
