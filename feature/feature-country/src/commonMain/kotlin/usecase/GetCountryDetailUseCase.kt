package usecase

import FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.dto.Country
import model.ui.CountryDetailUi
import model.ui.toCountryDetailUi
import org.koin.dsl.module
import repository.CountryRepository

val getCountryDetailUseCaseModule = module {
    single<GetCountryDetailUseCase> {
        GetCountryDetailUseCase(repository = get())
    }
}

class GetCountryDetailUseCase(
    private val repository: CountryRepository
): FlowUseCase<String, Country, CountryDetailUi>() {
    override suspend fun flowWork(input: String): Flow<Country> =
        repository.getCountries().map { countries ->
            requireNotNull(countries.find { it.code == input }) {
                "No country found for code $input"
            }
        }

    override suspend fun onSucceedDataHandling(intermediate: Country): UseCaseOutputWithStatus.Success<CountryDetailUi> {
        return UseCaseOutputWithStatus.Success(result = intermediate.toCountryDetailUi())
    }
}
