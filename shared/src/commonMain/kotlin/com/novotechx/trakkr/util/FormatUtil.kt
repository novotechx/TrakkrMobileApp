package com.novotechx.trakkr.util

import kotlin.math.abs
import kotlin.math.roundToLong

object FormatUtil {
    fun formatDuration(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0)
            "$hours:${pad2(minutes)}:${pad2(seconds)}"
        else
            "${pad2(minutes)}:${pad2(seconds)}"
    }

    fun formatDurationHoursMinutes(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        return "$hours:${pad2(minutes)}"
    }

    fun formatDistance(meters: Double): String {
        val km = meters / 1000.0
        return if (km >= 10) formatFixed(km, 1) else formatFixed(km, 2)
    }

    fun formatPace(secondsPerKm: Double): String {
        if (secondsPerKm <= 0 || secondsPerKm.isNaN() || secondsPerKm.isInfinite())
            return "--'--\""

        val totalSeconds = secondsPerKm.toLong()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes'${pad2(seconds)}\""
    }

    fun formatSpeed(kmh: Double): String = formatFixed(kmh, 1)

    fun formatElevation(meters: Double): String {
        val rounded = meters.roundToLong()
        val sign = if (rounded >= 0) "+" else "-"
        return "$sign${abs(rounded)}"
    }

    fun formatCalories(calories: Int): String = calories.toString()

    fun calculatePace(distanceMeters: Double, durationSeconds: Long): Double {
        if (distanceMeters <= 0) return 0.0
        val km = distanceMeters / 1000.0
        return durationSeconds / km
    }

    fun calculateSpeed(distanceMeters: Double, durationSeconds: Long): Double {
        if (durationSeconds <= 0) return 0.0
        val km = distanceMeters / 1000.0
        val hours = durationSeconds / 3600.0
        return km / hours
    }

    fun estimateCalories(
        distanceMeters: Double,
        durationSeconds: Long,
        type: String,
    ): Int {
        val hours = durationSeconds / 3600.0
        val met = when (type.uppercase()) {
            "RUN" -> 9.8
            "CYCLE" -> 7.5
            "WALK" -> 3.5
            "HIKE" -> 6.0
            else -> 5.0
        }
        return (met * 70 * hours).toInt()
    }

    private fun pad2(value: Long): String = if (value in 0..9) "0$value" else value.toString()

    private fun formatFixed(value: Double, decimals: Int): String {
        val factor = when (decimals) {
            1 -> 10L
            2 -> 100L
            else -> {
                var f = 1L
                repeat(decimals) { f *= 10 }
                f
            }
        }
        val negative = value < 0
        val scaled = (abs(value) * factor).roundToLong()
        val whole = scaled / factor
        val frac = scaled % factor
        val fracStr = frac.toString().padStart(decimals, '0')
        val sign = if (negative) "-" else ""
        return "$sign$whole.$fracStr"
    }
}
