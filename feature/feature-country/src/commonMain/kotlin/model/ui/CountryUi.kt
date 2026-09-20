package model.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CountriesUi(
    val items: ImmutableList<CountryListItem>,
    val allCountries: ImmutableList<CountryUi> = persistentListOf()
)

@Stable
data class CountryUi(
    val code: String,
    val name: String,
    val flagEmoji: String,
    val capital: String = ""
)

sealed interface CountryListItem {
    @Stable
    data class Header(val letter: String) : CountryListItem

    @Stable
    data class CountryRow(val country: CountryUi) : CountryListItem
}
