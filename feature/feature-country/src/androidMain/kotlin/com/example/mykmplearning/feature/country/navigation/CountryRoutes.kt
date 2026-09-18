package com.example.mykmplearning.feature.country.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CountriesRoute

@Serializable
data class CountryDetailRoute(val code: String)
