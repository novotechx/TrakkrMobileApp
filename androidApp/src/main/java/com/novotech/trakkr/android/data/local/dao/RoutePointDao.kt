package com.novotech.trakkr.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novotech.trakkr.android.data.local.entity.RoutePointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutePointDao
{
    @Query("SELECT * FROM route_points WHERE activity_id = :activityId ORDER BY timestamp ASC")
    fun getForActivity(activityId: Long): Flow<List<RoutePointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<RoutePointEntity>)

    @Query("DELETE FROM route_points WHERE activity_id = :activityId")
    suspend fun deleteForActivity(activityId: Long)
}
