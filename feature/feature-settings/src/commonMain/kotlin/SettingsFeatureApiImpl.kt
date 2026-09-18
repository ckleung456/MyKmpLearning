import model.di.settingsPresentationModule
import org.koin.dsl.module

class SettingsFeatureApiImpl : SettingsFeatureApi {

    override fun initialize() {
    }

    override fun cleanup() {
    }

    override val featureModule = module {
        includes(settingsPresentationModule)
    }
}
