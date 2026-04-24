package com.novotechx.trakkr.domain.repository

import com.novotechx.trakkr.domain.model.ActivityStats
import com.novotechx.trakkr.domain.model.ActivityType
import com.novotechx.trakkr.domain.model.RoutePoint
import com.novotechx.trakkr.domain.model.TrackingActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getAllActivities(): Flow<List<TrackingActivity>>

    fun getActivitiesByType(type: ActivityType): Flow<List<TrackingActivity>>

    fun getActivitiesInRange(startEpoch: Long, endEpoch: Long): Flow<List<TrackingActivity>>

    fun getActivityById(id: Long): Flow<TrackingActivity?>

    suspend fun insertActivity(activity: TrackingActivity): Long

    suspend fun updateActivity(activity: TrackingActivity)

    suspend fun deleteActivity(id: Long)

    suspend fun insertRoutePoints(points: List<RoutePoint>)

    fun getRoutePointsForActivity(activityId: Long): Flow<List<RoutePoint>>

    fun getStats(startEpoch: Long, endEpoch: Long): Flow<ActivityStats>

    fun getRecentActivities(limit: Int): Flow<List<TrackingActivity>>
}
