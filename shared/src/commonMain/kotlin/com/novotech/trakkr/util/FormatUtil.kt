package com.novotech.trakkr.util

object FormatUtil
{
    fun formatDuration(totalSeconds: Long): String
    {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0)
            "%d:%02d:%02d".format(hours, minutes, seconds)
        else
            "%02d:%02d".format(minutes, seconds)
    }

    fun formatDurationHoursMinutes(totalSeconds: Long): String
    {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        return "%d:%02d".format(hours, minutes)
    }

    fun formatDistance(meters: Double): String
    {
        val km = meters / 1000.0
        return if (km >= 10)
            "%.1f".format(km)
        else
            "%.2f".format(km)
    }

    fun formatPace(secondsPerKm: Double): String
    {
        if (secondsPerKm <= 0 || secondsPerKm.isNaN() || secondsPerKm.isInfinite())
            return "--'--\""

        val totalSeconds = secondsPerKm.toLong()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d'%02d\"".format(minutes, seconds)
    }

    fun formatSpeed(kmh: Double): String
    {
        return "%.1f".format(kmh)
    }

    fun formatElevation(meters: Double): String
    {
        return "%+.0f".format(meters)
    }

    fun formatCalories(calories: Int): String
    {
        return calories.toString()
    }

    fun calculatePace(distanceMeters: Double, durationSeconds: Long): Double
    {
        if (distanceMeters <= 0)
            return 0.0
        val km = distanceMeters / 1000.0
        return durationSeconds / km
    }

    fun calculateSpeed(distanceMeters: Double, durationSeconds: Long): Double
    {
        if (durationSeconds <= 0)
            return 0.0
        val km = distanceMeters / 1000.0
        val hours = durationSeconds / 3600.0
        return km / hours
    }

    fun estimateCalories(
        distanceMeters: Double,
        durationSeconds: Long,
        type: String,
    ): Int
    {
        val hours = durationSeconds / 3600.0
        val met = when (type.uppercase())
        {
            "RUN" -> 9.8
            "CYCLE" -> 7.5
            "WALK" -> 3.5
            "HIKE" -> 6.0
            else -> 5.0
        }
        // Rough estimate: MET * weight(70kg) * hours
        return (met * 70 * hours).toInt()
    }
}
