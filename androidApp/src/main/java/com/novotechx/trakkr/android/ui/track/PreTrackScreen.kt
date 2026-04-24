package com.novotechx.trakkr.android.ui.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotechx.trakkr.android.ui.components.ActivityTypeSelector
import com.novotechx.trakkr.android.ui.components.SectionLabel
import com.novotechx.trakkr.android.ui.components.StartButton
import com.novotechx.trakkr.android.ui.components.StatBox
import com.novotechx.trakkr.android.ui.components.TrakkrCard
import com.novotechx.trakkr.android.ui.components.TrakkrListItem
import com.novotechx.trakkr.android.ui.theme.TrakkrColors
import com.novotechx.trakkr.util.FormatUtil
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreTrackScreen(
    onStartTracking: (Int) -> Unit,
    onActivityClick: (Long) -> Unit,
    onViewAllClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: TrackingViewModel = koinViewModel(),
) {
    val selectedType by viewModel.selectedActivityType.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "Trakkr",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                )
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TrakkrColors.TextDim,
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TrakkrColors.TextDim,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
        ) {
            TrakkrCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SectionLabel("THIS WEEK")
                        Text(
                            "View all →",
                            fontSize = 10.sp,
                            color = TrakkrColors.TextDim,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        StatBox(
                            "DISTANCE",
                            FormatUtil.formatDistance(weeklyStats.totalDistanceMeters),
                            "km",
                            modifier = Modifier.weight(1f),
                        )
                        StatBox(
                            "ACTIVITIES",
                            weeklyStats.totalActivities.toString(),
                            modifier = Modifier.weight(1f),
                        )
                        StatBox(
                            "DURATION",
                            FormatUtil.formatDurationHoursMinutes(weeklyStats.totalDurationSeconds),
                            "hrs",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("ACTIVITY TYPE")
            ActivityTypeSelector(
                selectedIndex = selectedType,
                onSelect = { viewModel.selectActivityType(it) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            StartButton(onClick = { onStartTracking(selectedType) })

            Spacer(modifier = Modifier.height(16.dp))

            if (recentActivities.isNotEmpty()) {
                SectionLabel("RECENT")
                recentActivities.forEachIndexed { index, activity ->
                    TrakkrListItem(
                        icon = activity.type.emoji,
                        title = activity.title,
                        subtitle = "${FormatUtil.formatDistance(activity.distanceMeters)} km · ${FormatUtil.formatDuration(activity.durationSeconds)}",
                        rightText = FormatUtil.formatPace(activity.avgPaceSecondsPerKm),
                        showDivider = index < recentActivities.size - 1,
                        onClick = { onActivityClick(activity.id) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
