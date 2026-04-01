package com.novotech.trakkr.android.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.novotech.trakkr.android.service.TrackingService
import com.novotech.trakkr.android.ui.history.ActivityDetailScreen
import com.novotech.trakkr.android.ui.history.HistoryScreen
import com.novotech.trakkr.android.ui.history.StatisticsScreen
import com.novotech.trakkr.android.ui.onboarding.OnboardingScreen
import com.novotech.trakkr.android.ui.profile.ProfileScreen
import com.novotech.trakkr.android.ui.profile.SettingsScreen
import com.novotech.trakkr.android.ui.routes.RoutesScreen
import com.novotech.trakkr.android.ui.track.ActiveTrackingScreen
import com.novotech.trakkr.android.ui.track.PreTrackScreen
import com.novotech.trakkr.android.ui.track.SaveActivityScreen
import com.novotech.trakkr.android.ui.theme.TrakkrColors
import com.novotech.trakkr.domain.model.TrackingState

// ─── ROUTE DEFINITIONS ──────────────────────────────────────

object Routes
{
    const val ONBOARDING = "onboarding"
    const val PRE_TRACK = "pre_track"
    const val ACTIVE_TRACKING = "active_tracking"
    const val SAVE_ACTIVITY = "save_activity"
    const val HISTORY = "history"
    const val ACTIVITY_DETAIL = "activity_detail/{activityId}"
    const val STATISTICS = "statistics"
    const val ROUTES = "routes"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    fun activityDetail(id: Long) = "activity_detail/$id"
}

// ─── BOTTOM NAV ─────────────────────────────────────────────

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.PRE_TRACK, "Track", Icons.Default.MyLocation),
    BottomNavItem(Routes.HISTORY, "History", Icons.Default.History),
    BottomNavItem(Routes.ROUTES, "Routes", Icons.Default.Explore),
    BottomNavItem(Routes.PROFILE, "Profile", Icons.Default.Person),
)

private val screensWithBottomNav = setOf(
    Routes.PRE_TRACK,
    Routes.HISTORY,
    Routes.ROUTES,
    Routes.PROFILE,
)

@Composable
fun TrakkrBottomNav(
    navController: NavHostController,
    currentRoute: String?,
)
{
    Column
    {
        HorizontalDivider(color = TrakkrColors.Outline, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(TrakkrColors.Surface),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            bottomNavItems.forEach
            { item ->
                val active = currentRoute == item.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable
                        {
                            if (!active)
                            {
                                navController.navigate(item.route)
                                {
                                    popUpTo(Routes.PRE_TRACK) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                )
                {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (active) TrakkrColors.Gold else TrakkrColors.TextDim,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = item.label,
                        fontSize = 9.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        color = if (active) TrakkrColors.Gold else TrakkrColors.TextDim,
                    )
                }
            }
        }
    }
}

// ─── MAIN NAV HOST ──────────────────────────────────────────

@Composable
fun TrakkrNavHost(
    showOnboarding: Boolean,
    onOnboardingComplete: () -> Unit,
    onStartTracking: () -> Unit,
    onPauseTracking: () -> Unit,
    onResumeTracking: () -> Unit,
    onStopTracking: () -> Unit,
)
{
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val trackingState by TrackingService.trackingState.collectAsState()

    val showNav = currentRoute in screensWithBottomNav
        && trackingState == TrackingState.IDLE

    Scaffold(
        containerColor = TrakkrColors.Background,
        bottomBar =
        {
            AnimatedVisibility(
                visible = showNav,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            )
            {
                TrakkrBottomNav(navController, currentRoute)
            }
        },
    )
    { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        )
        {
            NavHost(
                navController = navController,
                startDestination = if (showOnboarding) Routes.ONBOARDING else Routes.PRE_TRACK,
            )
            {
                composable(Routes.ONBOARDING)
                {
                    OnboardingScreen(
                        onComplete =
                        {
                            onOnboardingComplete()
                            navController.navigate(Routes.PRE_TRACK)
                            {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        },
                    )
                }

                composable(Routes.PRE_TRACK)
                {
                    PreTrackScreen(
                        onStartTracking =
                        { activityTypeIndex ->
                            onStartTracking()
                            navController.navigate(Routes.ACTIVE_TRACKING)
                        },
                        onActivityClick =
                        { id ->
                            navController.navigate(Routes.activityDetail(id))
                        },
                        onViewAllClick =
                        {
                            navController.navigate(Routes.HISTORY)
                            {
                                popUpTo(Routes.PRE_TRACK) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onSettingsClick =
                        {
                            navController.navigate(Routes.SETTINGS)
                        },
                    )
                }

                composable(Routes.ACTIVE_TRACKING)
                {
                    ActiveTrackingScreen(
                        onPause = { onPauseTracking() },
                        onResume = { onResumeTracking() },
                        onStop =
                        {
                            onStopTracking()
                            navController.navigate(Routes.SAVE_ACTIVITY)
                            {
                                popUpTo(Routes.PRE_TRACK) { saveState = false }
                            }
                        },
                        onDiscard =
                        {
                            onStopTracking()
                            navController.popBackStack(Routes.PRE_TRACK, false)
                        },
                    )
                }

                composable(Routes.SAVE_ACTIVITY)
                {
                    SaveActivityScreen(
                        onSaved =
                        { activityId ->
                            navController.navigate(Routes.activityDetail(activityId))
                            {
                                popUpTo(Routes.PRE_TRACK) { saveState = false }
                            }
                        },
                        onDiscard =
                        {
                            navController.popBackStack(Routes.PRE_TRACK, false)
                        },
                    )
                }

                composable(Routes.HISTORY)
                {
                    HistoryScreen(
                        onActivityClick =
                        { id ->
                            navController.navigate(Routes.activityDetail(id))
                        },
                        onStatsClick =
                        {
                            navController.navigate(Routes.STATISTICS)
                        },
                    )
                }

                composable(
                    Routes.ACTIVITY_DETAIL,
                    arguments = listOf(navArgument("activityId") { type = NavType.LongType }),
                )
                { backStackEntry ->
                    val activityId = backStackEntry.arguments?.getLong("activityId") ?: 0L
                    ActivityDetailScreen(
                        activityId = activityId,
                        onBack = { navController.popBackStack() },
                    )
                }

                composable(Routes.STATISTICS)
                {
                    StatisticsScreen(
                        onBack = { navController.popBackStack() },
                        onRecordClick =
                        { id ->
                            navController.navigate(Routes.activityDetail(id))
                        },
                    )
                }

                composable(Routes.ROUTES)
                {
                    RoutesScreen()
                }

                composable(Routes.PROFILE)
                {
                    ProfileScreen(
                        onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    )
                }

                composable(Routes.SETTINGS)
                {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
