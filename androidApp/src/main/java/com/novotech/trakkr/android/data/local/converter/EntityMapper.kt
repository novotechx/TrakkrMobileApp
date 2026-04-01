package com.novotech.trakkr.android.data.local.converter

import com.novotech.trakkr.android.data.local.entity.ActivityEntity
import com.novotech.trakkr.android.data.local.entity.RoutePointEntity
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingActivity

object EntityMapper
{
    fun ActivityEntity.toDomain(): TrackingActivity
    {
        return TrackingActivity(
            id = id,
            type = ActivityType.fromOrdinal(type),
            title = title,
            notes = notes,
            startedAt = startedAt,
            endedAt = endedAt,
            distanceMeters = distanceMeters,
            durationSeconds = durationSeconds,
            avgPaceSecondsPerKm = avgPaceSecondsPerKm,
            avgSpeedKmh = avgSpeedKmh,
            elevationGainMeters = elevationGainMeters,
            calories = calories,
            weatherTemp = weatherTemp,
            weatherCondition = weatherCondition,
        )
    }

    fun TrackingActivity.toEntity(): ActivityEntity
    {
        return ActivityEntity(
            id = id,
            type = type.ordinal,
            title = title,
            notes = notes,
            startedAt = startedAt,
            endedAt = endedAt,
            distanceMeters = distanceMeters,
            durationSeconds = durationSeconds,
            avgPaceSecondsPerKm = avgPaceSecondsPerKm,
            avgSpeedKmh = avgSpeedKmh,
            elevationGainMeters = elevationGainMeters,
            calories = calories,
            weatherTemp = weatherTemp,
            weatherCondition = weatherCondition,
        )
    }

    fun RoutePointEntity.toDomain(): RoutePoint
    {
        return RoutePoint(
            id = id,
            activityId = activityId,
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            speed = speed,
            timestamp = timestamp,
            heartRate = heartRate,
        )
    }

    fun RoutePoint.toEntity(): RoutePointEntity
    {
        return RoutePointEntity(
            id = id,
            activityId = activityId,
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            speed = speed,
            timestamp = timestamp,
            heartRate = heartRate,
        )
    }
}
