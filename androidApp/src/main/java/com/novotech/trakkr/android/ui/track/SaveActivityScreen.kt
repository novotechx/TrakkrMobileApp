package com.novotech.trakkr.android.ui.track

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotech.trakkr.android.service.TrackingService
import com.novotech.trakkr.android.ui.components.PrimaryButton
import com.novotech.trakkr.android.ui.components.StatBox
import com.novotech.trakkr.android.ui.components.TrakkrCard
import com.novotech.trakkr.android.ui.components.TrakkrChip
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.util.DateTimeUtil
import com.novotech.trakkr.util.FormatUtil
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveActivityScreen(
    onSaved: (Long) -> Unit,
    onDiscard: () -> Unit,
    viewModel: TrackingViewModel = koinViewModel(),
)
{
    val routePoints by TrackingService.routePoints.collectAsState()
    val distance by TrackingService.distanceMeters.collectAsState()
    val duration by TrackingService.durationSeconds.collectAsState()
    val elevation by TrackingService.elevationGain.collectAsState()
    val selectedType by viewModel.selectedActivityType.collectAsState()

    val type = ActivityType.fromOrdinal(selectedType)
    val defaultTitle = "${DateTimeUtil.getTimeOfDayPrefix(DateTimeUtil.now())} ${type.displayName}"

    var title by remember { mutableStateOf(defaultTitle) }
    var notes by remember { mutableStateOf("") }
    var activityTypeIndex by remember { mutableIntStateOf(selectedType) }

    val pace = FormatUtil.calculatePace(distance, duration)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = TrakkrColors.Gold,
        unfocusedBorderColor = TrakkrColors.Outline,
        cursorColor = TrakkrColors.Gold,
        focusedContainerColor = TrakkrColors.SurfaceContainer,
        unfocusedContainerColor = TrakkrColors.SurfaceContainer,
        focusedTextColor = TrakkrColors.TextPrimary,
        unfocusedTextColor = TrakkrColors.TextPrimary,
        focusedPlaceholderColor = TrakkrColors.TextDim,
        unfocusedPlaceholderColor = TrakkrColors.TextDim,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background),
    )
    {
        TopAppBar(
            title = { Text("Save Activity", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            navigationIcon =
            {
                IconButton(onClick = onDiscard)
                {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TrakkrColors.TextDim,
                    )
                }
            },
            actions =
            {
                TextButton(
                    onClick =
                    {
                        viewModel.saveActivity(
                            routePoints = routePoints,
                            distanceMeters = distance,
                            durationSeconds = duration,
                            elevationGain = elevation,
                            title = title,
                            notes = notes,
                            onSaved = onSaved,
                        )
                    },
                )
                {
                    Text("Save", color = TrakkrColors.Gold, fontWeight = FontWeight.Bold)
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
                .height(140.dp)
                .background(TrakkrColors.SurfaceContainer),
            contentAlignment = Alignment.Center,
        )
        {
            Text(
                "Route preview",
                color = TrakkrColors.TextDim,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }

        // Stats summary bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TrakkrColors.Surface)
                .padding(vertical = 10.dp, horizontal = 12.dp),
        )
        {
            StatBox("DISTANCE", FormatUtil.formatDistance(distance), "km", modifier = Modifier.weight(1f))
            StatBox("DURATION", FormatUtil.formatDuration(duration), modifier = Modifier.weight(1f))
            StatBox("PACE", FormatUtil.formatPace(pace), "/km", modifier = Modifier.weight(1f))
        }
        HorizontalDivider(color = TrakkrColors.Outline)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
        )
        {
            // Title
            Text("TITLE", fontSize = 9.sp, color = TrakkrColors.TextDim, letterSpacing = 0.8.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = textFieldColors,
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Activity type chips
            Text("ACTIVITY TYPE", fontSize = 9.sp, color = TrakkrColors.TextDim, letterSpacing = 0.8.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp))
            {
                ActivityType.entries.take(4).forEachIndexed
                { index, actType ->
                    TrakkrChip(
                        label = "${actType.emoji} ${actType.displayName}",
                        active = index == activityTypeIndex,
                        onClick = { activityTypeIndex = index },
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notes
            Text("NOTES", fontSize = 9.sp, color = TrakkrColors.TextDim, letterSpacing = 0.8.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(10.dp),
                colors = textFieldColors,
                placeholder = { Text("Add notes about your activity...") },
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Weather + Photo row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                TrakkrCard(modifier = Modifier.weight(1f))
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Text("☀️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("22°C", fontSize = 10.sp, color = TrakkrColors.TextPrimary)
                        Text("Clear", fontSize = 8.sp, color = TrakkrColors.TextDim)
                    }
                }
                TrakkrCard(modifier = Modifier.weight(1f))
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Text("📸", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Add photos", fontSize = 10.sp, color = TrakkrColors.TextDim)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "✓  Save Activity",
                onClick =
                {
                    viewModel.saveActivity(
                        routePoints = routePoints,
                        distanceMeters = distance,
                        durationSeconds = duration,
                        elevationGain = elevation,
                        title = title,
                        notes = notes,
                        onSaved = onSaved,
                    )
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Discard Activity",
                fontSize = 11.sp,
                color = TrakkrColors.Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}
