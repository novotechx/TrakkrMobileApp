package com.novotechx.trakkr.android.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotechx.trakkr.android.ui.components.SectionLabel
import com.novotechx.trakkr.android.ui.components.StatBox
import com.novotechx.trakkr.android.ui.components.TrakkrCard
import com.novotechx.trakkr.android.ui.components.TrakkrListItem
import com.novotechx.trakkr.android.ui.theme.TrakkrColors
import com.novotechx.trakkr.domain.model.ActivityStats
import com.novotechx.trakkr.domain.repository.ActivityRepository
import com.novotechx.trakkr.util.DateTimeUtil
import com.novotechx.trakkr.util.FormatUtil
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
) {
    val repository = koinInject<ActivityRepository>()
    val allTimeStats by repository.getStats(0L, DateTimeUtil.now())
        .collectAsState(initial = ActivityStats())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(TrakkrColors.Background),
    ) {
        TopAppBar(
            title = { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, "Settings", tint = TrakkrColors.TextDim)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text(
                "👤",
                fontSize = 40.sp,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TrakkrColors.GoldSubtle),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Local User",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TrakkrColors.TextPrimary,
            )
            Text(
                "No account — data stored locally",
                fontSize = 11.sp,
                color = TrakkrColors.TextDim,
            )
        }

        TrakkrCard(modifier = Modifier.padding(horizontal = 12.dp)) {
            Column {
                SectionLabel("ALL-TIME STATS")
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    StatBox("DISTANCE", FormatUtil.formatDistance(allTimeStats.totalDistanceMeters), "km", modifier = Modifier.weight(1f))
                    StatBox("ACTIVITIES", allTimeStats.totalActivities.toString(), modifier = Modifier.weight(1f))
                    StatBox("DURATION", FormatUtil.formatDurationHoursMinutes(allTimeStats.totalDurationSeconds), "hrs", modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            val menuItems = listOf(
                "📊" to "Statistics & Records",
                "🗺️" to "Offline Maps",
                "📤" to "Export Data",
                "🎯" to "Goals & Challenges",
                "🔔" to "Notifications",
                "⚙️" to "Settings",
                "📋" to "Privacy Policy",
                "ℹ️" to "About Trakkr",
            )

            menuItems.forEachIndexed { index, (icon, label) ->
                TrakkrListItem(
                    icon = icon,
                    title = label,
                    rightText = if (label == "About Trakkr") "v1.0 →" else "→",
                    rightColor = TrakkrColors.Gold,
                    showDivider = index < menuItems.size - 1,
                    onClick = { if (label == "Settings") onSettingsClick() },
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
