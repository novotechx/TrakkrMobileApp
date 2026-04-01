plugins
{
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin
{
    androidTarget
    {
        compilations.all
        {
            kotlinOptions
            {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach
    {
        it.binaries.framework
        {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets
    {
        commonMain.dependencies
        {
            implementation(libs.coroutines.core)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization.json)
        }
        androidMain.dependencies
        {
            implementation(libs.ktor.android)
            implementation(libs.coroutines.android)
        }
        iosMain.dependencies
        {
            implementation(libs.ktor.ios)
        }
    }
}

android
{
    namespace = "com.novotech.trakkr.shared"
    compileSdk = 35
    defaultConfig
    {
        minSdk = 26
    }
    compileOptions
    {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
