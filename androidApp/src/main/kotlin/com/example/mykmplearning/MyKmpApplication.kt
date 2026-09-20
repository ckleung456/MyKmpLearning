package com.example.mykmplearning

import CountryFeatureApiImpl
import FeatureRegistry
import SettingsFeatureApiImpl
import android.app.Application
import initKoin
import kotlinx.coroutines.runBlocking
import model.FeatureApi
import model.FeatureDao
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules

class MyKmpApplication : Application() {
    private val featureRegistry: FeatureRegistry by inject()
    private val featureDao: FeatureDao by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MyKmpApplication)
        }

        initFeatures(
            features = listOf(
                CountryFeatureApiImpl(featureDao = featureDao),
                SettingsFeatureApiImpl()
            )
        )
    }

    private fun initFeatures(features: List<FeatureApi>) {
        loadKoinModules(features.map { it.featureModule })
        runBlocking {
            features.forEach { featureApi ->
                featureRegistry.registerFeature(featureApi)
            }
        }
    }
}
