package com.novotechx.trakkr.android.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotechx.trakkr.android.ui.components.SectionLabel
import com.novotechx.trakkr.android.ui.components.TrakkrCard
import com.novotechx.trakkr.android.ui.theme.TrakkrColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background),
    ) {
        TopAppBar(
            title = { Text("Settings", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TrakkrColors.TextDim)
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
            SectionLabel("TRACKING")
            SettingsSection(
                items = listOf(
                    "Auto-pause" to "On",
                    "Auto-pause sensitivity" to "Medium",
                    "GPS accuracy" to "High",
                    "Screen always on" to "Off",
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionLabel("DISPLAY")
            SettingsSection(
                items = listOf(
                    "Units" to "Metric (km)",
                    "Theme" to "Dark",
                    "Map style" to "Street",
                    "Audio cues" to "Every 1 km",
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionLabel("DATA")
            SettingsSection(
                items = listOf(
                    "Export all data (GPX)" to "",
                    "Clear all data" to "",
                ),
                dangerousItems = setOf("Clear all data"),
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    items: List<Pair<String, String>>,
    dangerousItems: Set<String> = emptySet(),
) {
    TrakkrCard {
        Column {
            items.forEachIndexed { index, (label, value) ->
                val isDangerous = label in dangerousItems
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 8.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        label,
                        fontSize = 12.sp,
                        color = if (isDangerous) TrakkrColors.Error else TrakkrColors.TextPrimary,
                    )
                    if (value.isNotEmpty()) {
                        Text(
                            value,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TrakkrColors.Gold,
                        )
                    }
                }
                if (index < items.size - 1)
                    HorizontalDivider(color = TrakkrColors.Outline)
            }
        }
    }
}
