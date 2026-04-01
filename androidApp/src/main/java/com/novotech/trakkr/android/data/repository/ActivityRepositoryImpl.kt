package com.novotech.trakkr.android.data.repository

import com.novotech.trakkr.android.data.local.converter.EntityMapper.toDomain
import com.novotech.trakkr.android.data.local.converter.EntityMapper.toEntity
import com.novotech.trakkr.android.data.local.dao.ActivityDao
import com.novotech.trakkr.android.data.local.dao.RoutePointDao
import com.novotech.trakkr.domain.model.ActivityStats
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingActivity
import com.novotech.trakkr.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRepositoryImpl(
    private val activityDao: ActivityDao,
    private val routePointDao: RoutePointDao,
) : ActivityRepository
{
    override fun getAllActivities(): Flow<List<TrackingActivity>>
    {
        return activityDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getActivitiesByType(type: ActivityType): Flow<List<TrackingActivity>>
    {
        return activityDao.getByType(type.ordinal).map { list -> list.map { it.toDomain() } }
    }

    override fun getActivitiesInRange(
        startEpoch: Long,
        endEpoch: Long,
    ): Flow<List<TrackingActivity>>
    {
        return activityDao.getInRange(startEpoch, endEpoch)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getActivityById(id: Long): Flow<TrackingActivity?>
    {
        return activityDao.getById(id).map { it?.toDomain() }
    }

    override suspend fun insertActivity(activity: TrackingActivity): Long
    {
        return activityDao.insert(activity.toEntity())
    }

    override suspend fun updateActivity(activity: TrackingActivity)
    {
        activityDao.update(activity.toEntity())
    }

    override suspend fun deleteActivity(id: Long)
    {
        activityDao.deleteById(id)
    }

    override suspend fun insertRoutePoints(points: List<RoutePoint>)
    {
        routePointDao.insertAll(points.map { it.toEntity() })
    }

    override fun getRoutePointsForActivity(activityId: Long): Flow<List<RoutePoint>>
    {
        return routePointDao.getForActivity(activityId)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getStats(startEpoch: Long, endEpoch: Long): Flow<ActivityStats>
    {
        return activityDao.getStatsRaw(startEpoch, endEpoch).map { raw ->
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
    }

    override fun getRecentActivities(limit: Int): Flow<List<TrackingActivity>>
    {
        return activityDao.getRecent(limit).map { list -> list.map { it.toDomain() } }
    }
}
