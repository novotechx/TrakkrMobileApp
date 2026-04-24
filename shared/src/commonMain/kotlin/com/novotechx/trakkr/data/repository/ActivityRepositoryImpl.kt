package com.novotechx.trakkr.data.repository

import com.novotechx.trakkr.data.local.dao.ActivityDao
import com.novotechx.trakkr.data.local.dao.RoutePointDao
import com.novotechx.trakkr.data.local.toDomain
import com.novotechx.trakkr.data.local.toEntity
import com.novotechx.trakkr.domain.model.ActivityStats
import com.novotechx.trakkr.domain.model.ActivityType
import com.novotechx.trakkr.domain.model.RoutePoint
import com.novotechx.trakkr.domain.model.TrackingActivity
import com.novotechx.trakkr.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRepositoryImpl(
    private val activityDao: ActivityDao,
    private val routePointDao: RoutePointDao,
) : ActivityRepository {
    override fun getAllActivities(): Flow<List<TrackingActivity>> =
        activityDao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getActivitiesByType(type: ActivityType): Flow<List<TrackingActivity>> =
        activityDao.getByType(type.ordinal).map { list -> list.map { it.toDomain() } }

    override fun getActivitiesInRange(
        startEpoch: Long,
        endEpoch: Long,
    ): Flow<List<TrackingActivity>> =
        activityDao.getInRange(startEpoch, endEpoch)
            .map { list -> list.map { it.toDomain() } }

    override fun getActivityById(id: Long): Flow<TrackingActivity?> =
        activityDao.getById(id).map { it?.toDomain() }

    override suspend fun insertActivity(activity: TrackingActivity): Long =
        activityDao.insert(activity.toEntity())

    override suspend fun updateActivity(activity: TrackingActivity) {
        activityDao.update(activity.toEntity())
    }

    override suspend fun deleteActivity(id: Long) {
        activityDao.deleteById(id)
    }

    override suspend fun insertRoutePoints(points: List<RoutePoint>) {
        routePointDao.insertAll(points.map { it.toEntity() })
    }

    override fun getRoutePointsForActivity(activityId: Long): Flow<List<RoutePoint>> =
        routePointDao.getForActivity(activityId)
            .map { list -> list.map { it.toDomain() } }

    override fun getStats(startEpoch: Long, endEpoch: Long): Flow<ActivityStats> =
        activityDao.getStatsRaw(startEpoch, endEpoch).map { raw ->
            val avgPace = if (raw.totalDistance > 0)
                raw.totalDuration.toDouble() / (raw.totalDistance / 1000.0)
            else
                0.0

            ActivityStats(
                totalDistanceMeters = raw.totalDistance,
                totalActivities = raw.totalCount,
                totalDurationSeconds = raw.totalDuration,
                avgPaceSecondsPerKm = avgPace,
            )
        }

    override fun getRecentActivities(limit: Int): Flow<List<TrackingActivity>> =
        activityDao.getRecent(limit).map { list -> list.map { it.toDomain() } }
}
