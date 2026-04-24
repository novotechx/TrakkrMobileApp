package com.novotechx.trakkr.android.ui.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotechx.trakkr.android.ui.components.OutlineButton
import com.novotechx.trakkr.android.ui.theme.TrakkrColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Routes", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
            actions = {
                TextButton(onClick = {}) {
                    Text(
                        "+ New",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TrakkrColors.Gold,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TrakkrColors.Background,
                titleContentColor = TrakkrColors.TextPrimary,
            ),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("🗺️", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No routes yet",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TrakkrColors.TextPrimary,
                )
                Text(
                    "Create your first route or import a GPX file",
                    fontSize = 12.sp,
                    color = TrakkrColors.TextDim,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlineButton(
                    text = "📂  Import GPX / KML",
                    onClick = {},
                )
            }
        }
    }
}
