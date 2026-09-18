import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.example.mykmplearning.core.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // Coroutines (Flow, Mutex, StateFlow)
            api(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.compose.material3)
        }
        iosMain.dependencies {
            // ObserveAsEvents (Utils.kt) is androidMain-only, but the Compose
            // Compiler plugin still runs its IR extension on every target
            // this module declares - without compose-runtime here too, the
            // iOS compile fails with IncompatibleComposeRuntimeVersionException
            // even though no iOS code actually uses @Composable.
            implementation(libs.compose.runtime)
        }
    }
}
