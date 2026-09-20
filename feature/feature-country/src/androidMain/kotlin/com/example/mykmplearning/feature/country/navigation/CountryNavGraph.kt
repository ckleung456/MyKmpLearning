package com.example.mykmplearning.feature.country.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mykmplearning.feature.country.ui.CountriesRoot
import com.example.mykmplearning.feature.country.ui.CountryDetailRoot
import model.network.CountryFeatureConstant.COUNTRY_FEATURE_SCOPE
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

fun NavGraphBuilder.countriesGraph(navController: NavController) {
    composable<CountriesRoute> {
        val koin = getKoin()
        val featureScope = remember {
            koin.getOrCreateScope(scopeId = "CountriesId", qualifier = named(COUNTRY_FEATURE_SCOPE))
        }
        CountriesRoot(
            onNavigateToDetail = { code ->
                navController.navigate(CountryDetailRoute(code = code))
            },
            viewModel = koinViewModel(
                scope = featureScope
            )
        )
    }
    composable<CountryDetailRoute>(
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) { backStackEntry ->
        val koin = getKoin()
        val featureScope = remember {
            koin.getOrCreateScope(scopeId = "CountryDetailId", qualifier = named(COUNTRY_FEATURE_SCOPE))
        }
        val route: CountryDetailRoute = backStackEntry.toRoute()
        CountryDetailRoot(
            code = route.code,
            onBack = { navController.popBackStack() },
            viewModel = koinViewModel(
                scope = featureScope,
                parameters = { parametersOf(route.code) }
            )
        )
    }
}
