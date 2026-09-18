package com.example.mykmplearning.ios

import CountryFeatureApiImpl
import SettingsFeatureApiImpl
import org.koin.core.context.loadKoinModules

// iOS features are statically linked into the Shared framework (see
// core:registry's FeatureLoader iosMain actual), so there is no dynamic
// discovery step here - just load each feature's Koin module directly.
// Instances are retained here (not local to start()) in case Swift ever
// needs to reach a concrete feature instance directly; cross-feature
// navigation itself goes through FeatureEventBus (see IosServices), not
// through these instances.
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
