package com.example.mykmplearning

import FeatureEventBus
import OpenCountriesEvent
import Utils.ObserveAsEvents
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.rememberNavController
import com.example.mykmplearning.feature.country.navigation.CountriesRoute
import com.example.mykmplearning.feature.country.navigation.countriesGraph
import com.example.mykmplearning.feature.settings.navigation.SettingsRoute
import com.example.mykmplearning.feature.settings.navigation.settingsGraph
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val featureEventBus: FeatureEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainApp(featureEventBus = featureEventBus)
            }
        }
    }
}

@Composable
private fun MainApp(featureEventBus: FeatureEventBus) {
    val navController: NavHostController = rememberNavController()

    ObserveAsEvents(featureEventBus.events, key1 = featureEventBus) { event ->
        when (event) {
            is OpenCountriesEvent -> navController.navigateToTab(CountriesRoute)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.statusBars),
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination

            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<CountriesRoute>() } == true,
                    onClick = { navController.navigateToTab(CountriesRoute) },
                    icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Countries") },
                    label = { Text("Countries") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<SettingsRoute>() } == true,
                    onClick = { navController.navigateToTab(SettingsRoute) },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CountriesRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            countriesGraph(navController)
            settingsGraph()
        }
    }
}

private fun NavHostController.navigateToTab(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
