package usecase

import FlowUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import model.dto.Country
import model.ui.CountriesUi
import model.ui.toCountryUi
import model.ui.toGroupedCountryListItems
import repository.CountryRepository

class GetCountriesUseCase(
    private val repository: CountryRepository
): FlowUseCase<Unit, List<Country>, CountriesUi>(){
    override suspend fun flowWork(input: Unit): Flow<List<Country>> = repository.getCountries()

    override suspend fun onSucceedDataHandling(intermediate: List<Country>): UseCaseOutputWithStatus.Success<CountriesUi> {
        val allCountries = intermediate.map { it.toCountryUi() }.toImmutableList()
        return UseCaseOutputWithStatus.Success(
            result = CountriesUi(
                items = allCountries.toGroupedCountryListItems(),
                allCountries = allCountries
            )
        )
    }
}
