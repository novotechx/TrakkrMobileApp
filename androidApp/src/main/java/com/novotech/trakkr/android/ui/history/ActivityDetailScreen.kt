package com.novotech.trakkr.android.ui.history

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotech.trakkr.android.ui.components.SectionLabel
import com.novotech.trakkr.android.ui.components.StatBox
import com.novotech.trakkr.android.ui.components.TrakkrCard
import com.novotech.trakkr.android.ui.components.TrakkrChip
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.domain.model.Split
import com.novotech.trakkr.util.FormatUtil
import com.novotech.trakkr.util.GeoUtil
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    activityId: Long,
    onBack: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel(),
)
{
    val activityState = remember { viewModel.getActivityById(activityId) }
    val activity by activityState.collectAsState()
    val routePointsState = remember { viewModel.getRoutePoints(activityId) }
    val routePoints by routePointsState.collectAsState()

    val a = activity ?: return

    // Calculate splits from route points
    val splits = remember(routePoints)
    {
        if (routePoints.size < 2)
            return@remember emptyList<Split>()

        val result = mutableListOf<Split>()
        var kmDistance = 0.0
        var kmStartIndex = 0
        var currentKm = 1

        for (i in 1 until routePoints.size)
        {
            val prev = routePoints[i - 1]
            val curr = routePoints[i]
            kmDistance += GeoUtil.distanceBetween(prev.latitude, prev.longitude, curr.latitude, curr.longitude)

            if (kmDistance >= 1000)
            {
                val segDuration = (curr.timestamp - routePoints[kmStartIndex].timestamp) / 1000.0
                val elevChange = curr.altitude - routePoints[kmStartIndex].altitude
                result.add(
                    Split(
                        kmNumber = currentKm,
                        paceSecondsPerKm = segDuration,
                        elevationChange = elevChange,
                    )
                )
                currentKm++
                kmDistance -= 1000
                kmStartIndex = i
            }
        }
        result
    }

    val bestPace = splits.minOfOrNull { it.paceSecondsPerKm } ?: 1.0
    val worstPace = splits.maxOfOrNull { it.paceSecondsPerKm } ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background),
    )
    {
        TopAppBar(
            title = { Text(a.title, fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            navigationIcon =
            {
                IconButton(onClick = onBack)
                {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TrakkrColors.TextDim)
                }
            },
            actions =
            {
                IconButton(onClick = {}) { Icon(Icons.Default.Edit, "Edit", tint = TrakkrColors.TextDim) }
                IconButton(onClick = {}) { Icon(Icons.Default.Share, "Share", tint = TrakkrColors.TextDim) }
                IconButton(onClick = { viewModel.deleteActivity(activityId); onBack() })
                {
                    Icon(Icons.Default.Delete, "Delete", tint = TrakkrColors.Error)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        // Map placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(TrakkrColors.SurfaceContainer),
            contentAlignment = Alignment.Center,
        )
        {
            Text(
                "Route map (${routePoints.size} points)",
                color = TrakkrColors.TextDim,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
        )
        {
            // Stats row 1
            Row(modifier = Modifier.fillMaxWidth())
            {
                StatBox("DISTANCE", FormatUtil.formatDistance(a.distanceMeters), "km", large = true, modifier = Modifier.weight(1f))
                StatBox("DURATION", FormatUtil.formatDuration(a.durationSeconds), large = true, modifier = Modifier.weight(1f))
                StatBox("AVG PACE", FormatUtil.formatPace(a.avgPaceSecondsPerKm), "/km", large = true, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Stats row 2
            Row(modifier = Modifier.fillMaxWidth())
            {
                StatBox("ELEVATION", FormatUtil.formatElevation(a.elevationGainMeters), "m", modifier = Modifier.weight(1f))
                StatBox("CALORIES", FormatUtil.formatCalories(a.calories), "kcal", modifier = Modifier.weight(1f))
                StatBox("AVG SPEED", FormatUtil.formatSpeed(a.avgSpeedKmh), "km/h", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Splits
            if (splits.isNotEmpty())
            {
                SectionLabel("SPLITS")
                TrakkrCard
                {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                    )
                    {
                        Text("KM", fontSize = 9.sp, color = TrakkrColors.TextDim, modifier = Modifier.width(30.dp))
                        Text("PACE", fontSize = 9.sp, color = TrakkrColors.TextDim, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text("ELEV", fontSize = 9.sp, color = TrakkrColors.TextDim, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                    HorizontalDivider(color = TrakkrColors.Outline)

                    splits.forEach
                    { split ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        )
                        {
                            Text(
                                "${split.kmNumber}",
                                fontSize = 11.sp,
                                color = TrakkrColors.TextDim,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.width(30.dp),
                            )
                            Text(
                                FormatUtil.formatPace(split.paceSecondsPerKm),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TrakkrColors.TextPrimary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                "${FormatUtil.formatElevation(split.elevationChange)}m",
                                fontSize = 10.sp,
                                color = TrakkrColors.TextDim,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                            )
                            // Progress bar
                            val barFraction = if (worstPace > bestPace)
                                1f - ((split.paceSecondsPerKm - bestPace) / (worstPace - bestPace)).toFloat()
                            else
                                1f
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(TrakkrColors.SurfaceContainer),
                            )
                            {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(barFraction.coerceIn(0.1f, 1f))
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(TrakkrColors.Gold),
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather
            if (a.weatherTemp != null)
            {
                SectionLabel("WEATHER")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
                {
                    TrakkrChip(label = "☀️ ${a.weatherTemp?.toInt()}°C")
                }
            }

            // Notes
            if (a.notes.isNotBlank())
            {
                Spacer(modifier = Modifier.height(12.dp))
                SectionLabel("NOTES")
                Text(a.notes, fontSize = 12.sp, color = TrakkrColors.TextPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
