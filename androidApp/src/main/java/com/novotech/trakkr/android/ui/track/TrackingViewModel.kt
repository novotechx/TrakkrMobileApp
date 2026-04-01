package com.novotech.trakkr.android.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novotech.trakkr.domain.model.ActivityStats
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingActivity
import com.novotech.trakkr.domain.repository.ActivityRepository
import com.novotech.trakkr.domain.usecase.SaveActivityUseCase
import com.novotech.trakkr.util.DateTimeUtil
import com.novotech.trakkr.util.FormatUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackingViewModel(
    private val saveActivityUseCase: SaveActivityUseCase,
    private val repository: ActivityRepository,
) : ViewModel()
{
    private val m_selectedActivityType = MutableStateFlow(0)
    val selectedActivityType: StateFlow<Int> = m_selectedActivityType.asStateFlow()

    val weeklyStats: StateFlow<ActivityStats> = repository.getStats(
        DateTimeUtil.weekStartEpoch(),
        DateTimeUtil.now(),
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ActivityStats(),
    )

    val recentActivities: StateFlow<List<TrackingActivity>> = repository.getRecentActivities(3)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList(),
        )

    fun selectActivityType(index: Int)
    {
        m_selectedActivityType.value = index
    }

    fun saveActivity(
        routePoints: List<RoutePoint>,
        distanceMeters: Double,
        durationSeconds: Long,
        elevationGain: Double,
        title: String,
        notes: String,
        onSaved: (Long) -> Unit,
    )
    {
        viewModelScope.launch
        {
            val type = ActivityType.fromOrdinal(m_selectedActivityType.value)
            val now = DateTimeUtil.now()
            val startedAt = now - (durationSeconds * 1000)

            val activity = TrackingActivity(
                type = type,
                title = title.ifBlank
                {
                    "${DateTimeUtil.getTimeOfDayPrefix(startedAt)} ${type.displayName}"
                },
                notes = notes,
                startedAt = startedAt,
                endedAt = now,
                distanceMeters = distanceMeters,
                durationSeconds = durationSeconds,
                avgPaceSecondsPerKm = FormatUtil.calculatePace(distanceMeters, durationSeconds),
                avgSpeedKmh = FormatUtil.calculateSpeed(distanceMeters, durationSeconds),
                elevationGainMeters = elevationGain,
                calories = FormatUtil.estimateCalories(
                    distanceMeters, durationSeconds, type.name,
                ),
            )

            val id = saveActivityUseCase(activity, routePoints)
            onSaved(id)
        }
    }
}
