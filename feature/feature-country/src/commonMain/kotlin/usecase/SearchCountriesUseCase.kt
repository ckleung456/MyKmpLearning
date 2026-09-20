package usecase

import FlowUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import model.ui.CountryListItem
import model.ui.CountryUi
import model.ui.toGroupedCountryListItems

class SearchCountriesUseCase: FlowUseCase<SearchCountriesUseCase.Input, List<CountryUi>, ImmutableList<CountryListItem>>() {
    override suspend fun flowWork(input: Input): Flow<List<CountryUi>> {
        val (query, countries) = input
        val filtered =if (query.isBlank()) {
            countries
        } else {
            countries.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.capital.contains(query, ignoreCase = true)
            }
        }
        return flowOf(filtered)
    }

    override suspend fun onSucceedDataHandling(intermediate: List<CountryUi>): UseCaseOutputWithStatus.Success<ImmutableList<CountryListItem>> =
        UseCaseOutputWithStatus.Success(intermediate.toGroupedCountryListItems())

    data class Input(
        val query: String = "",
        val countries: List<CountryUi> = emptyList()
    )
}