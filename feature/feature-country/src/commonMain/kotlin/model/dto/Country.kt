package model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val capital: String? = null,
    val code: String? = null,
    val currency: CountryCurrency? = null,
    val flag: String? = null,
    val language: CountryLanguage? = null,
    val name: String? = null,
    val region: String? = null
)

@Serializable
data class CountryCurrency(
    val code: String? = null,
    val name: String? = null,
    val symbol: String? = null
)

@Serializable
data class CountryLanguage(
    val code: String? = null,
    val name: String? = null
)
