package com.novotech.trakkr.domain.usecase

import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingActivity
import com.novotech.trakkr.domain.repository.ActivityRepository

class SaveActivityUseCase(private val repository: ActivityRepository)
{
    suspend operator fun invoke(
        activity: TrackingActivity,
        routePoints: List<RoutePoint>,
    ): Long
    {
        val activityId = repository.insertActivity(activity)
        val pointsWithId = routePoints.map { it.copy(activityId = activityId) }
        repository.insertRoutePoints(pointsWithId)
        return activityId
    }
}
