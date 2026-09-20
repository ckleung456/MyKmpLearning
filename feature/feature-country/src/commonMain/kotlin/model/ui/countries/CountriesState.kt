package model.ui.countries

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import model.ui.CountryListItem

@Stable
data class CountriesState(
    val countries: ImmutableList<CountryListItem> = persistentListOf(),
    val query: String = "",
    val isSearching: Boolean = false
)