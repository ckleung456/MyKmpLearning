package model.di

import SettingsFeatureApi
import SettingsFeatureApiImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.SettingsViewModel

internal val settingsPresentationModule = module {
    viewModelOf(::SettingsViewModel)
}

val settingsFeatureApiModule = module {
    single<SettingsFeatureApi> {
        SettingsFeatureApiImpl(featureDao = get())
    }
    includes(settingsPresentationModule)
}