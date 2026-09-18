package com.example.mykmplearning.ios

import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin
import viewmodel.CountriesViewModel
import viewmodel.CountryDetailViewModel
import viewmodel.SettingsViewModel

// Small, concrete (non-generic) factory functions Swift can call to obtain
// Koin-provided ViewModels - reified generics don't bridge cleanly to Swift.
object IosViewModels {
    fun countries(): CountriesViewModel = getKoin().get()

    fun countryDetail(code: String): CountryDetailViewModel =
        getKoin().get(parameters = { parametersOf(code) })

    fun settings(): SettingsViewModel = getKoin().get()
}
