package com.example.mykmplearning.ios

import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import viewmodel.CountriesViewModel
import viewmodel.CountryDetailViewModel
import viewmodel.SettingsViewModel

// Small, concrete (non-generic) factory functions Swift can call to obtain
// Koin-provided ViewModels - reified generics don't bridge cleanly to Swift.
object IosViewModels : KoinComponent {
    private const val COUNTRIES_SCOPE_QUALIFIER = "feature-country"

    fun countries(): CountriesViewModel {
        val featureScope = getKoin().getOrCreateScope(
            scopeId = "countriesId_iOS",
            qualifier = named(COUNTRIES_SCOPE_QUALIFIER)
        )
        return featureScope.get()
    }

    fun countryDetail(code: String): CountryDetailViewModel {
        val featureScope = getKoin().getOrCreateScope(
            scopeId = "countryDetailId_iOS",
            qualifier = named(COUNTRIES_SCOPE_QUALIFIER)
        )
        return featureScope.get { parametersOf(code) }
    }

    fun settings(): SettingsViewModel = getKoin().get()
}