import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import model.di.countryFeatureDataModule
import model.di.countryFeaturePresentationModule
import org.koin.dsl.module


class CountryFeatureApiImpl : CountryFeatureApi {

    private val _openCountriesEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val openCountriesEvents = _openCountriesEvents.asSharedFlow()

    override fun openCountries() {
        _openCountriesEvents.tryEmit(Unit)
    }

    override fun initialize() { }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun cleanup() {
        _openCountriesEvents.resetReplayCache()
    }

    override val featureModule = module {
        includes(
            countryFeatureDataModule,
            countryFeaturePresentationModule
        )
    }
}