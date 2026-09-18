package com.example.mykmplearning.feature.country.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mykmplearning.feature.country.ui.CountriesRoot
import com.example.mykmplearning.feature.country.ui.CountryDetailRoot

fun NavGraphBuilder.countriesGraph(navController: NavController) {
    composable<CountriesRoute> {
        CountriesRoot(
            onNavigateToDetail = { code ->
                navController.navigate(CountryDetailRoute(code = code))
            }
        )
    }
    composable<CountryDetailRoute>(
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) { backStackEntry ->
        val route: CountryDetailRoute = backStackEntry.toRoute()
        CountryDetailRoot(
            code = route.code,
            onBack = { navController.popBackStack() }
        )
    }
}
