package com.novotechx.trakkr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.novotechx.trakkr.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY started_at DESC")
    fun getAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE type = :type ORDER BY started_at DESC")
    fun getByType(type: Int): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE started_at >= :startEpoch AND started_at <= :endEpoch ORDER BY started_at DESC")
    fun getInRange(startEpoch: Long, endEpoch: Long): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :id")
    fun getById(id: Long): Flow<ActivityEntity?>

    @Query("SELECT * FROM activities ORDER BY started_at DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity): Long

    @Update
    suspend fun update(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
        SELECT COALESCE(SUM(distance_meters), 0.0) AS totalDistance,
               COUNT(*) AS totalCount,
               COALESCE(SUM(duration_seconds), 0) AS totalDuration
        FROM activities
        WHERE started_at >= :startEpoch AND started_at <= :endEpoch
        """
    )
    fun getStatsRaw(startEpoch: Long, endEpoch: Long): Flow<StatsProjection>
}
