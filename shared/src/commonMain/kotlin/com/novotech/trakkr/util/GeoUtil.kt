package com.novotech.trakkr.util

import kotlin.math.*

object GeoUtil
{
    private const val EARTH_RADIUS_METERS = 6_371_000.0

    /**
     * Haversine distance between two GPS coordinates in meters.
     */
    fun distanceBetween(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double,
    ): Double
    {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }
}
