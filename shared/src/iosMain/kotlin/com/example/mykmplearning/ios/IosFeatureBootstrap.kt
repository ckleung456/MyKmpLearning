package com.example.mykmplearning.ios

import org.koin.core.context.loadKoinModules

// iOS features are statically linked into the Shared framework (see
// core:registry's FeatureLoader iosMain actual), so there is no dynamic
// discovery step here - just load each feature's Koin module directly.
// Instances are retained here (not local to start()) so Swift can reach the
// same CountryFeatureApi instance later, e.g. to call openCountries() from a
// Settings screen the same way FeatureRegistry.getFeature<CountryFeatureApi>()
// does on Android.
object IosFeatureBootstrap {
    val countryFeature = CountryFeatureApiImpl()
    val settingsFeature = SettingsFeatureApiImpl()

    fun start() {
        loadKoinModules(
            listOf(
                countryFeature.featureModule,
                settingsFeature.featureModule
            )
        )
    }
}
