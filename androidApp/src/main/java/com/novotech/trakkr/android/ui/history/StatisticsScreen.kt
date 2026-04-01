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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotech.trakkr.android.ui.components.SectionLabel
import com.novotech.trakkr.android.ui.components.StatBox
import com.novotech.trakkr.android.ui.components.TrakkrCard
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.util.DateTimeUtil
import com.novotech.trakkr.util.FormatUtil
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    onRecordClick: (Long) -> Unit,
    viewModel: HistoryViewModel = koinViewModel(),
)
{
    val periodFilter by viewModel.periodFilter.collectAsState()
    val periodStats by viewModel.periodStats.collectAsState()
    val activities by viewModel.activities.collectAsState()

    // Compute daily distances for bar chart (last 30 days)
    val dailyDistances = activities
        .filter
        { act ->
            act.startedAt >= DateTimeUtil.monthStartEpoch()
        }
        .groupBy
        { act ->
            DateTimeUtil.epochToLocalDate(act.startedAt).dayOfMonth
        }
        .mapValues
        { (_, acts) ->
            acts.sumOf { it.distanceMeters }
        }

    val maxDayDistance = dailyDistances.values.maxOrNull() ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background),
    )
    {
        TopAppBar(
            title = { Text("Statistics", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            navigationIcon =
            {
                IconButton(onClick = onBack)
                {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TrakkrColors.TextDim)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        // Period tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        )
        {
            listOf("W" to PeriodFilter.WEEK, "M" to PeriodFilter.MONTH, "Y" to PeriodFilter.YEAR, "All" to PeriodFilter.ALL)
                .forEach
                { (label, period) ->
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
                            label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (active) TrakkrColors.Background else TrakkrColors.TextDim,
                        )
                    }
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
        )
        {
            // Bar chart
            TrakkrCard
            {
                Text(
                    "DISTANCE (KM)",
                    fontSize = 10.sp,
                    color = TrakkrColors.TextDim,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom,
                )
                {
                    for (day in 1..31)
                    {
                        val dist = dailyDistances[day] ?: 0.0
                        val fraction = if (maxDayDistance > 0) (dist / maxDayDistance).toFloat() else 0f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height((fraction * 80).dp.coerceAtLeast(2.dp))
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (dist > 0) TrakkrColors.Gold.copy(alpha = 0.6f)
                                    else TrakkrColors.SurfaceContainer
                                ),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                )
                {
                    Text("1", fontSize = 8.sp, color = TrakkrColors.TextDim)
                    Text("15", fontSize = 8.sp, color = TrakkrColors.TextDim)
                    Text("31", fontSize = 8.sp, color = TrakkrColors.TextDim)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Summary stats
            TrakkrCard
            {
                Row(modifier = Modifier.fillMaxWidth())
                {
                    StatBox("TOTAL DIST", FormatUtil.formatDistance(periodStats.totalDistanceMeters), "km", modifier = Modifier.weight(1f))
                    StatBox("ACTIVITIES", periodStats.totalActivities.toString(), modifier = Modifier.weight(1f))
                    StatBox("AVG PACE", FormatUtil.formatPace(periodStats.avgPaceSecondsPerKm), "/km", modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal records
            SectionLabel("PERSONAL RECORDS 🏆")
            TrakkrCard
            {
                val records = listOf(
                    "Fastest 5K" to activities
                        .filter { it.distanceMeters in 4900.0..5200.0 }
                        .minByOrNull { it.avgPaceSecondsPerKm },
                    "Longest Run" to activities
                        .filter { it.type.ordinal == 0 }
                        .maxByOrNull { it.distanceMeters },
                    "Longest Ride" to activities
                        .filter { it.type.ordinal == 1 }
                        .maxByOrNull { it.distanceMeters },
                    "Highest Climb" to activities
                        .maxByOrNull { it.elevationGainMeters },
                )

                records.forEachIndexed
                { index, (label, record) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (record != null) Modifier.clickable { onRecordClick(record.id) }
                                else Modifier
                            )
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        Text("🏅", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f))
                        {
                            Text(label, fontSize = 11.sp, color = TrakkrColors.TextPrimary)
                            if (record != null)
                            {
                                Text(
                                    DateTimeUtil.formatDateRelative(record.startedAt),
                                    fontSize = 9.sp,
                                    color = TrakkrColors.TextDim,
                                )
                            }
                        }
                        Text(
                            when
                            {
                                record == null -> "—"
                                label.contains("Pace") || label.contains("5K") ->
                                    FormatUtil.formatDuration(record.durationSeconds)
                                label.contains("Climb") ->
                                    "+${record.elevationGainMeters.toInt()} m"
                                else ->
                                    "${FormatUtil.formatDistance(record.distanceMeters)} km"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TrakkrColors.Gold,
                        )
                    }
                    if (index < records.size - 1)
                        HorizontalDivider(color = TrakkrColors.Outline.copy(alpha = 0.3f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
