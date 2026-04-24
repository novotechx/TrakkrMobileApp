package com.novotechx.trakkr.android.ui.onboarding

import android.Manifest
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.novotechx.trakkr.android.ui.components.PrimaryButton
import com.novotechx.trakkr.android.ui.components.TrakkrCard
import com.novotechx.trakkr.android.ui.theme.TrakkrColors
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val emoji: String,
    val headline: String,
    val body: String,
)

private val pages = listOf(
    OnboardingPage(
        emoji = "🛤️",
        headline = "Your Activity.\nYour Way.",
        body = "Track runs, rides, walks & hikes with zero social pressure. Your data stays on your device.",
    ),
    OnboardingPage(
        emoji = "📡",
        headline = "Works Offline",
        body = "GPS tracking works without internet. Download maps for fully offline use.",
    ),
    OnboardingPage(
        emoji = "⏱️",
        headline = "Just Press Start",
        body = "No accounts, no social feeds, no clutter. Simple tracking that gets out of your way.",
    ),
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size + 1 })
    val scope = rememberCoroutineScope()
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrakkrColors.Background)
            .padding(horizontal = 20.dp, vertical = 40.dp),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                if (page < pages.size) {
                    val p = pages[page]
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(TrakkrColors.GoldSubtle),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(p.emoji, fontSize = 48.sp)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            p.headline,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TrakkrColors.TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            p.body,
                            fontSize = 13.sp,
                            color = TrakkrColors.TextDim,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }
                }
                else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(TrakkrColors.GoldSubtle),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("📍", fontSize = 40.sp)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Location Access",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrakkrColors.TextPrimary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Trakkr needs location access to track your activities. GPS works offline.",
                            fontSize = 12.sp,
                            color = TrakkrColors.TextDim,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        TrakkrCard {
                            Column {
                                listOf(
                                    "🔒" to "Your location is never shared",
                                    "📱" to "Only used during active tracking",
                                    "💾" to "Stored locally on your device",
                                ).forEach { (icon, text) ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(icon, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text, fontSize = 11.sp, color = TrakkrColors.TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pages.size + 1) { index ->
                val active = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(if (active) 24.dp else 8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (active) TrakkrColors.Gold else TrakkrColors.SurfaceContainer),
                )
                if (index < pages.size) Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (pagerState.currentPage < pages.size) {
            PrimaryButton(
                text = "Get Started",
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pages.size) }
                },
            )
        }
        else {
            PrimaryButton(
                text = if (locationPermission.status.isGranted) "Continue" else "Allow Location",
                onClick = {
                    if (locationPermission.status.isGranted)
                        onComplete()
                    else
                        locationPermission.launchPermissionRequest()
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "You can change this in Settings anytime",
                fontSize = 11.sp,
                color = TrakkrColors.TextDim,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
