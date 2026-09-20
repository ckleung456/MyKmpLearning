package model.di

import FeatureEventBus
import org.koin.dsl.module

val featureRegistryModule = module {
    single<FeatureEventBus> {
        FeatureEventBus()
    }
}