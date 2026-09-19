import model.di.settingsPresentationModule
import org.koin.dsl.module

class SettingsFeatureApiImpl : SettingsFeatureApi {
    override val featureModule = module {
        includes(settingsPresentationModule)
    }
}
