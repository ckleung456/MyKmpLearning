package usecase

import FlowUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import model.dto.Country
import model.ui.CountriesUi
import model.ui.CountryListItem
import model.ui.toCountryUi
import org.koin.dsl.module
import repository.CountryRepository

val getCountriesUseCaseModule = module {
    single<GetCountriesUseCase> {
        GetCountriesUseCase(repository = get())
    }
}

class GetCountriesUseCase(
    private val repository: CountryRepository
): FlowUseCase<Unit, List<Country>, CountriesUi>(){
    override suspend fun flowWork(input: Unit): Flow<List<Country>> = repository.getCountries()

    override suspend fun onSucceedDataHandling(intermediate: List<Country>): UseCaseOutputWithStatus.Success<CountriesUi> {
        val grouped = intermediate.groupBy { nameGroupLetter(it.name) }
        val items = grouped.keys
            .sortedWith(compareBy({ it == FALLBACK_LETTER }, { it }))
            .flatMap { letter ->
                val countriesInGroup = grouped.getValue(letter)
                    .sortedWith(compareBy({ it.capital.orEmpty() }, { it.name.orEmpty() }))
                listOf(CountryListItem.Header(letter = letter)) +
                    countriesInGroup.map { CountryListItem.CountryRow(country = it.toCountryUi()) }
            }
            .toImmutableList()
        return UseCaseOutputWithStatus.Success(result = CountriesUi(items = items))
    }

    private fun nameGroupLetter(name: String?): String {
        val first = name?.trim()?.firstOrNull()
        return if (first == null || !first.isLetter()) FALLBACK_LETTER else first.uppercaseChar().toString()
    }

    companion object {
        private const val FALLBACK_LETTER = "#"
    }
}
