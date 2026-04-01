package com.novotech.trakkr.android.ui.track

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotech.trakkr.android.service.TrackingService
import com.novotech.trakkr.android.ui.components.CircleButton
import com.novotech.trakkr.android.ui.components.OutlineButton
import com.novotech.trakkr.android.ui.components.StatBox
import com.novotech.trakkr.android.ui.components.StatusBadge
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.domain.model.TrackingState
import com.novotech.trakkr.util.FormatUtil

@Composable
fun ActiveTrackingScreen(
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onDiscard: () -> Unit,
)
{
    val trackingState by TrackingService.trackingState.collectAsState()
    val distance by TrackingService.distanceMeters.collectAsState()
    val duration by TrackingService.durationSeconds.collectAsState()
    val elevation by TrackingService.elevationGain.collectAsState()
    val speed by TrackingService.currentSpeed.collectAsState()
    var showDiscardDialog by remember { mutableStateOf(false) }

    val isPaused = trackingState == TrackingState.PAUSED
    val pace = FormatUtil.calculatePace(distance, duration)
    val avgSpeed = FormatUtil.calculateSpeed(distance, duration)
    val calories = FormatUtil.estimateCalories(distance, duration, "RUN")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background),
    )
    {
        // Map placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(TrakkrColors.SurfaceContainer),
            contentAlignment = Alignment.Center,
        )
        {
            Text(
                "MapLibre View\n(GPS route rendered here)",
                color = TrakkrColors.TextDim,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }

        // Activity info bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TrakkrColors.Surface)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Text("🏃", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "RUNNING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TrakkrColors.Gold,
                )
            }
            if (isPaused)
            {
                StatusBadge("● PAUSED", TrakkrColors.Error, TrakkrColors.ErrorBg)
            }
            else
            {
                StatusBadge("● GPS", TrakkrColors.Success, TrakkrColors.SuccessBg)
            }
        }

        // Paused banner
        if (isPaused)
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TrakkrColors.GoldSubtle)
                    .border(width = 0.dp, color = TrakkrColors.GoldBorder)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            )
            {
                Text(
                    "⏸  PAUSED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TrakkrColors.Gold,
                    letterSpacing = 1.sp,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            // Primary stat: Duration
            Text(
                text = FormatUtil.formatDuration(duration),
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = if (isPaused) TrakkrColors.TextDim else TrakkrColors.TextPrimary,
                letterSpacing = 2.sp,
            )
            Text(
                "DURATION",
                fontSize = 10.sp,
                color = TrakkrColors.TextDim,
                letterSpacing = 1.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Distance + Pace row
            Row(modifier = Modifier.fillMaxWidth())
            {
                StatBox(
                    "DISTANCE",
                    FormatUtil.formatDistance(distance),
                    "km",
                    large = true,
                    modifier = Modifier.weight(1f),
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(48.dp),
                    color = TrakkrColors.Outline,
                )
                StatBox(
                    "PACE",
                    FormatUtil.formatPace(pace),
                    "/km",
                    large = true,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Elevation + Calories + Speed row
            Row(modifier = Modifier.fillMaxWidth())
            {
                StatBox(
                    "ELEVATION",
                    FormatUtil.formatElevation(elevation),
                    "m",
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    "CALORIES",
                    FormatUtil.formatCalories(calories),
                    "kcal",
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    "AVG SPEED",
                    FormatUtil.formatSpeed(avgSpeed),
                    "km/h",
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Control buttons
            if (isPaused)
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    // Stop
                    CircleButton(
                        size = 56.dp,
                        onClick = onStop,
                        borderColor = TrakkrColors.Error,
                    )
                    {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = TrakkrColors.Error,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    // Resume
                    CircleButton(
                        size = 72.dp,
                        onClick = onResume,
                        useGoldGradient = true,
                    )
                    {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Resume",
                            tint = TrakkrColors.Background,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                    // Discard
                    CircleButton(
                        size = 56.dp,
                        onClick = { showDiscardDialog = true },
                        borderColor = TrakkrColors.Outline,
                    )
                    {
                        Text("🗑", fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Share Live / Add Photo (phase 1 placeholders)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
                {
                    Box(modifier = Modifier.weight(1f))
                    {
                        OutlineButton("📸 Add Photo", onClick = {})
                    }
                }
            }
            else
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    // Lap
                    CircleButton(
                        size = 48.dp,
                        onClick = { /* TODO: lap */ },
                        borderColor = TrakkrColors.Outline,
                    )
                    {
                        Text(
                            "LAP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TrakkrColors.TextDim,
                        )
                    }
                    // Pause
                    CircleButton(
                        size = 64.dp,
                        onClick = onPause,
                        useGoldGradient = true,
                    )
                    {
                        Icon(
                            Icons.Default.Pause,
                            contentDescription = "Pause",
                            tint = TrakkrColors.Background,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    // Lock
                    CircleButton(
                        size = 48.dp,
                        onClick = { /* TODO: lock */ },
                        borderColor = TrakkrColors.Outline,
                    )
                    {
                        Text("🔓", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Discard confirmation dialog
    if (showDiscardDialog)
    {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard Activity") },
            text = { Text("Discard this activity? This cannot be undone.") },
            confirmButton =
            {
                TextButton(
                    onClick =
                    {
                        showDiscardDialog = false
                        onDiscard()
                    },
                )
                {
                    Text("Discard", color = TrakkrColors.Error)
                }
            },
            dismissButton =
            {
                TextButton(onClick = { showDiscardDialog = false })
                {
                    Text("Cancel")
                }
            },
            containerColor = TrakkrColors.Surface,
        )
    }
}
