import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.example.mykmplearning.feature.country"
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
            implementation(project(":core:core-network"))
            implementation(project(":core:core-domain"))
            implementation(project(":core:core-presentation"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client)
            implementation(libs.serialization.json)
            implementation(libs.compose.runtime)
            api(project(":feature:feature-country-api"))
            implementation(project(":core:registry"))
            implementation(project(":core:core-presentation"))
            implementation(libs.kotlinx.imutable)
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsCore)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.junit)
            implementation(libs.androidx.espresso.core)
            implementation(libs.androidx.testExt.junit)
        }
    }
}
