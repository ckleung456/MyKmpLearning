package model.ui

import kotlinx.collections.immutable.toImmutableList
import model.dto.Country

private const val NOT_AVAILABLE = "Not available"

fun Country.toCountryUi(): CountryUi = CountryUi(
    code = code.orEmpty(),
    name = name ?: "Unknown country",
    flagEmoji = countryCodeToFlagEmoji(code)
)

fun Country.toCountryDetailUi(): CountryDetailUi = CountryDetailUi(
    flagEmoji = countryCodeToFlagEmoji(code),
    fields = listOf(
        DetailField(label = "Name", value = name ?: NOT_AVAILABLE),
        DetailField(label = "Code", value = code ?: NOT_AVAILABLE),
        DetailField(label = "Capital", value = capital ?: NOT_AVAILABLE),
        DetailField(label = "Region", value = region ?: NOT_AVAILABLE),
        DetailField(label = "Currency", value = formatCurrency()),
        DetailField(label = "Language", value = formatLanguage())
    ).toImmutableList()
)

private fun Country.formatCurrency(): String {
    val currency = currency ?: return NOT_AVAILABLE
    val name = currency.name
    val details = listOfNotNull(currency.code, currency.symbol)
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = ", ")
    return when {
        name != null && details != null -> "$name ($details)"
        name != null -> name
        details != null -> details
        else -> NOT_AVAILABLE
    }
}

private fun Country.formatLanguage(): String {
    val language = language ?: return NOT_AVAILABLE
    val name = language.name
    val code = language.code
    return when {
        name != null && code != null -> "$name ($code)"
        name != null -> name
        code != null -> code
        else -> NOT_AVAILABLE
    }
}
