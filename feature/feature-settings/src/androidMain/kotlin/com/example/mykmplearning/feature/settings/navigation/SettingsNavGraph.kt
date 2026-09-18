package com.example.mykmplearning.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mykmplearning.feature.settings.ui.SettingsRoot

fun NavGraphBuilder.settingsGraph() {
    composable<SettingsRoute> {
        SettingsRoot()
    }
}
