package com.novotechx.trakkr.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: Int,
    val title: String,
    val notes: String = "",
    @ColumnInfo(name = "started_at")
    val startedAt: Long,
    @ColumnInfo(name = "ended_at")
    val endedAt: Long,
    @ColumnInfo(name = "distance_meters")
    val distanceMeters: Double,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Long,
    @ColumnInfo(name = "avg_pace")
    val avgPaceSecondsPerKm: Double = 0.0,
    @ColumnInfo(name = "avg_speed")
    val avgSpeedKmh: Double = 0.0,
    @ColumnInfo(name = "elevation_gain")
    val elevationGainMeters: Double = 0.0,
    val calories: Int = 0,
    @ColumnInfo(name = "weather_temp")
    val weatherTemp: Double? = null,
    @ColumnInfo(name = "weather_condition")
    val weatherCondition: String? = null,
)
