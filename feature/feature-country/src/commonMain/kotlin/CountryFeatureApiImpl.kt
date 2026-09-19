import model.di.countryFeatureDataModule
import model.di.countryFeaturePresentationModule
import org.koin.dsl.module

class CountryFeatureApiImpl : CountryFeatureApi {
    private val allowRouteSet = setOf(
        "SettingsRoute"
    )

    override val featureModule = module {
        includes(
            countryFeatureDataModule,
            countryFeaturePresentationModule
        )
    }

    override fun canHandleRoute(route: String): Boolean = route in allowRouteSet

}
