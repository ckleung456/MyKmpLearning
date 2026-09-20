import model.FeatureApi
import model.FeatureAvailability
import org.koin.core.module.Module
import org.koin.dsl.module

interface SettingsFeatureApi: FeatureApi {
    override val featureId: String
        get() = "SettingsFeatureApi"

    override val availability: FeatureAvailability
        get() = FeatureAvailability.AVAILABLE

    override val version: String
        get() = "1.0.0"

    override val dependencies: List<String>
        get() = emptyList()

    override val featureModule: Module
        get() = module {  }
}
