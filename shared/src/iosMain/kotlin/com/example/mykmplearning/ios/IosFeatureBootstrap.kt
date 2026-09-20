package com.example.mykmplearning.ios

import CountryFeatureApiImpl
import FeatureRegistry
import SettingsFeatureApiImpl
import kotlinx.coroutines.runBlocking
import org.koin.core.context.loadKoinModules
import org.koin.mp.KoinPlatform.getKoin

// iOS features are statically linked into the Shared framework (see
// core:registry's FeatureLoader iosMain actual), so there is no dynamic
// discovery step here - just load each feature's Koin module directly.
// Instances are retained here (not local to start()) in case Swift ever
// needs to reach a concrete feature instance directly; cross-feature
// navigation itself goes through FeatureEventBus (see IosServices), but
// features are still registered into FeatureRegistry so shared code that
// looks features up (e.g. version/availability checks) works identically
// on both platforms.
object IosFeatureBootstrap {
    val countryFeature = CountryFeatureApiImpl(featureDao = getKoin().get())
    val settingsFeature = SettingsFeatureApiImpl()

    fun start() {
        val features = listOf(countryFeature, settingsFeature)
        loadKoinModules(features.map { it.featureModule })
        runBlocking {
            val registry: FeatureRegistry = getKoin().get()
            features.forEach { registry.registerFeature(it) }
        }
    }
}
