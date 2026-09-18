import model.di.countryFeatureDataModule
import model.di.countryFeaturePresentationModule
import org.koin.dsl.module

class CountryFeatureApiImpl : CountryFeatureApi {

    override fun initialize() { }

    override fun cleanup() { }

    override val featureModule = module {
        includes(
            countryFeatureDataModule,
            countryFeaturePresentationModule
        )
    }
}
