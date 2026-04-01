# Trakkr — Your Activity. Your Way.

A privacy-first, offline-capable activity tracking app built with Kotlin Multiplatform.

## Project Structure

```
trakkr/
├── shared/                         # KMP shared module
│   └── src/
│       ├── commonMain/kotlin/      # Shared business logic
│       │   ├── domain/model/       # ActivityType, TrackingActivity, RoutePoint, etc.
│       │   ├── domain/repository/  # ActivityRepository interface
│       │   ├── domain/usecase/     # SaveActivity, GetActivities, GetStats, Delete
│       │   └── util/               # FormatUtil, DateTimeUtil, GeoUtil
│       ├── androidMain/            # Android-specific implementations
│       └── iosMain/                # iOS stubs (ready for Phase 5)
├── androidApp/                     # Android Compose UI
│   └── src/main/java/.../
│       ├── data/local/             # Room DB: entities, DAOs, database
│       ├── data/repository/        # ActivityRepositoryImpl
│       ├── di/                     # Koin dependency injection
│       ├── service/                # LocationTracker, TrackingService (foreground)
│       ├── ui/theme/               # TrakkrColors, Typography, Theme
│       ├── ui/components/          # Reusable: StatBox, Cards, Chips, Buttons
│       ├── ui/navigation/          # Bottom nav, routes, NavHost
│       ├── ui/track/               # PreTrack, ActiveTracking, SaveActivity
│       ├── ui/history/             # History, ActivityDetail, Statistics
│       ├── ui/routes/              # Routes (Phase 3 placeholder)
│       ├── ui/profile/             # Profile, Settings
│       └── ui/onboarding/          # Welcome carousel + location permission
└── iosApp/                         # iOS app (Phase 5)
```

## Phase 1 Scope (This Build)

- GPS tracking with foreground service (start/pause/resume/stop)
- Real-time distance, pace, speed, elevation, calories, duration
- Activity save with editable title, notes, type
- Activity history with date grouping, type filters, period filters
- Activity detail with route stats and splits
- Statistics dashboard with bar chart and personal records
- Dark theme UI matching NovoTech brand system
- Onboarding carousel with location permission
- Profile screen, Settings screen
- Room database (local-only, no cloud sync)
- Koin DI, Coroutines + StateFlow

## Getting Started

1. Open in Android Studio Ladybug (2024.2+)
2. Sync Gradle
3. Run on device or emulator (API 26+)
4. Grant location permission in onboarding

### Firebase Setup (Optional for Phase 1)
1. Create Firebase project
2. Download `google-services.json` to `androidApp/`
3. Uncomment Firebase plugins & dependencies in `androidApp/build.gradle.kts`

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Shared Logic | Kotlin Multiplatform |
| Android UI | Jetpack Compose + Material 3 |
| Local DB | Room |
| DI | Koin |
| Location | FusedLocationProviderClient |
| Maps | MapLibre (tiles: OpenFreeMap) |
| Architecture | Clean Architecture (domain/data/presentation) |

## Design System

- Background: `#121212`
- Surface: `#1E1E1E`
- Primary Gold: `#E1A500`
- Text Primary: `#E8E2D6`
- Monospace stats, Inter/Roboto body text
- See `Trakkr_UI_UX_Design_Spec.docx` for full spec

## License

Proprietary — NovoTech (Pty) Ltd. All rights reserved.
