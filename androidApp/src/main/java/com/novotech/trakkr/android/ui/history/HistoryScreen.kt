package com.novotech.trakkr.android.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotech.trakkr.android.ui.components.SectionLabel
import com.novotech.trakkr.android.ui.components.StatBox
import com.novotech.trakkr.android.ui.components.TrakkrCard
import com.novotech.trakkr.android.ui.components.TrakkrChip
import com.novotech.trakkr.android.ui.components.TrakkrListItem
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.util.DateTimeUtil
import com.novotech.trakkr.util.FormatUtil
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onActivityClick: (Long) -> Unit,
    onStatsClick: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel(),
)
{
    val activities by viewModel.activities.collectAsState()
    val typeFilter by viewModel.typeFilter.collectAsState()
    val periodFilter by viewModel.periodFilter.collectAsState()
    val periodStats by viewModel.periodStats.collectAsState()

    // Group activities by date
    val grouped = activities.groupBy { DateTimeUtil.formatDateRelative(it.startedAt) }

    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar(
            title = { Text("History", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            actions =
            {
                IconButton(onClick = {})
                {
                    Icon(Icons.Default.Search, "Search", tint = TrakkrColors.TextDim)
                }
                IconButton(onClick = onStatsClick)
                {
                    Icon(Icons.Default.BarChart, "Statistics", tint = TrakkrColors.TextDim)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        // Filter chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
        {
            item
            {
                TrakkrChip(
                    label = "All",
                    active = typeFilter == null,
                    onClick = { viewModel.setTypeFilter(null) },
                )
            }
            items(ActivityType.entries.toList().dropLast(1))
            { type ->
                TrakkrChip(
                    label = "${type.emoji} ${type.displayName}",
                    active = typeFilter == type,
                    onClick = { viewModel.setTypeFilter(type) },
                )
            }
        }

        // Period selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        )
        {
            PeriodFilter.entries.forEach
            { period ->
                val active = periodFilter == period
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (active) TrakkrColors.Gold else TrakkrColors.Background)
                        .clickable { viewModel.setPeriodFilter(period) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                )
                {
                    Text(
                        period.name.take(if (period == PeriodFilter.ALL) 3 else 5)
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (active) TrakkrColors.Background else TrakkrColors.TextDim,
                    )
                }
            }
        }

        // Period summary
        TrakkrCard(
            highlight = true,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
        {
            Row(modifier = Modifier.fillMaxWidth())
            {
                StatBox(
                    "DISTANCE",
                    FormatUtil.formatDistance(periodStats.totalDistanceMeters),
                    "km",
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    "ACTIVITIES",
                    periodStats.totalActivities.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    "DURATION",
                    FormatUtil.formatDurationHoursMinutes(periodStats.totalDurationSeconds),
                    "hrs",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Activity list
        if (activities.isEmpty())
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            )
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text("🏃", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No activities yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrakkrColors.TextPrimary,
                    )
                    Text(
                        "Start tracking to see your history here",
                        fontSize = 12.sp,
                        color = TrakkrColors.TextDim,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        else
        {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            {
                grouped.forEach
                { (dateLabel, dayActivities) ->
                    item { SectionLabel(dateLabel.uppercase()) }
                    items(dayActivities)
                    { activity ->
                        TrakkrListItem(
                            icon = activity.type.emoji,
                            title = activity.title,
                            subtitle = "${FormatUtil.formatDistance(activity.distanceMeters)} km · ${FormatUtil.formatDuration(activity.durationSeconds)} · ${FormatUtil.formatPace(activity.avgPaceSecondsPerKm)}/km",
                            rightText = "→",
                            rightColor = TrakkrColors.Gold,
                            onClick = { onActivityClick(activity.id) },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}
