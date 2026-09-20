package com.example.mykmplearning

import android.app.Application
import initKoin
import model.di.countryFeatureApiModule
import model.di.settingsFeatureApiModule
import org.koin.android.ext.koin.androidContext

class MyKmpApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MyKmpApplication)
            modules(
                countryFeatureApiModule,
                settingsFeatureApiModule
            )
        }
    }
}
