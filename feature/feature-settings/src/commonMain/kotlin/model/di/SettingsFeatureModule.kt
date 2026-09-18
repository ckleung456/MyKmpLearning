package model.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.SettingsViewModel

val settingsPresentationModule = module {
    viewModelOf(::SettingsViewModel)
}
