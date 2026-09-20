package model.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val FALLBACK_LETTER = "#"

fun List<CountryUi>.toGroupedCountryListItems(): ImmutableList<CountryListItem> {
    val grouped = groupBy { nameGroupLetter(it.name) }
    return grouped.keys
        .sortedWith(compareBy({ it == FALLBACK_LETTER }, { it }))
        .flatMap { letter ->
            val countriesInGroup = grouped.getValue(letter)
                .sortedWith(compareBy({ it.capital }, { it.name }))
            listOf(CountryListItem.Header(letter = letter)) +
                countriesInGroup.map { CountryListItem.CountryRow(country = it) }
        }
        .toImmutableList()
}

private fun nameGroupLetter(name: String): String {
    val first = name.trim().firstOrNull()
    return if (first == null || !first.isLetter()) FALLBACK_LETTER else first.uppercaseChar().toString()
}
