package com.novotech.trakkr.android.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novotech.trakkr.domain.model.ActivityStats
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingActivity
import com.novotech.trakkr.domain.repository.ActivityRepository
import com.novotech.trakkr.domain.usecase.DeleteActivityUseCase
import com.novotech.trakkr.domain.usecase.GetActivitiesUseCase
import com.novotech.trakkr.util.DateTimeUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PeriodFilter
{
    WEEK, MONTH, YEAR, ALL
}

class HistoryViewModel(
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val repository: ActivityRepository,
) : ViewModel()
{
    private val m_typeFilter = MutableStateFlow<ActivityType?>(null)
    val typeFilter: StateFlow<ActivityType?> = m_typeFilter.asStateFlow()

    private val m_periodFilter = MutableStateFlow(PeriodFilter.MONTH)
    val periodFilter: StateFlow<PeriodFilter> = m_periodFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activities: StateFlow<List<TrackingActivity>> = m_typeFilter
        .flatMapLatest
        { type ->
            getActivitiesUseCase(type = type)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val periodStats: StateFlow<ActivityStats> = m_periodFilter
        .flatMapLatest
        { period ->
            val start = when (period)
            {
                PeriodFilter.WEEK -> DateTimeUtil.weekStartEpoch()
                PeriodFilter.MONTH -> DateTimeUtil.monthStartEpoch()
                PeriodFilter.YEAR -> DateTimeUtil.yearStartEpoch()
                PeriodFilter.ALL -> 0L
            }
            repository.getStats(start, DateTimeUtil.now())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActivityStats())

    fun setTypeFilter(type: ActivityType?)
    {
        m_typeFilter.value = type
    }

    fun setPeriodFilter(period: PeriodFilter)
    {
        m_periodFilter.value = period
    }

    fun getActivityById(id: Long) = repository.getActivityById(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun getRoutePoints(activityId: Long): StateFlow<List<RoutePoint>> =
        repository.getRoutePointsForActivity(activityId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteActivity(id: Long)
    {
        viewModelScope.launch
        {
            deleteActivityUseCase(id)
        }
    }
}
