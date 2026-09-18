import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import model.di.countriesPresentationModule
import model.di.countryPresentationModule
import org.koin.dsl.module
import repository.countryRepositoryModule
import usecase.getCountriesUseCaseModule
import usecase.getCountryDetailUseCaseModule


class CountryFeatureApiImpl : CountryFeatureApi {

    private val _openCountriesEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val openCountriesEvents = _openCountriesEvents.asSharedFlow()

    override fun openCountries() {
        _openCountriesEvents.tryEmit(Unit)
    }

    override fun initialize() {

    }

    override fun cleanup() {

    }

    override val featureModule = module {
        includes(
            countryRepositoryModule,
            getCountriesUseCaseModule,
            getCountryDetailUseCaseModule,
            countriesPresentationModule,
            countryPresentationModule
        )
    }
}