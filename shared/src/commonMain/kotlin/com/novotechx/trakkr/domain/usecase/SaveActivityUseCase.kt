package com.novotechx.trakkr.domain.usecase

import com.novotechx.trakkr.domain.model.RoutePoint
import com.novotechx.trakkr.domain.model.TrackingActivity
import com.novotechx.trakkr.domain.repository.ActivityRepository

class SaveActivityUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(
        activity: TrackingActivity,
        routePoints: List<RoutePoint>,
    ): Long {
        val activityId = repository.insertActivity(activity)
        val pointsWithId = routePoints.map { it.copy(activityId = activityId) }
        repository.insertRoutePoints(pointsWithId)
        return activityId
    }
}
