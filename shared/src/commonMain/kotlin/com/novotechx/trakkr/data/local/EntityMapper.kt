package com.novotechx.trakkr.data.local

import com.novotechx.trakkr.data.local.entity.ActivityEntity
import com.novotechx.trakkr.data.local.entity.RoutePointEntity
import com.novotechx.trakkr.domain.model.ActivityType
import com.novotechx.trakkr.domain.model.RoutePoint
import com.novotechx.trakkr.domain.model.TrackingActivity

internal fun ActivityEntity.toDomain(): TrackingActivity = TrackingActivity(
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

internal fun TrackingActivity.toEntity(): ActivityEntity = ActivityEntity(
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

internal fun RoutePointEntity.toDomain(): RoutePoint = RoutePoint(
    id = id,
    activityId = activityId,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    speed = speed,
    timestamp = timestamp,
    heartRate = heartRate,
)

internal fun RoutePoint.toEntity(): RoutePointEntity = RoutePointEntity(
    id = id,
    activityId = activityId,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    speed = speed,
    timestamp = timestamp,
    heartRate = heartRate,
)
