package com.example.mykmplearning.ios

import FeatureEventBus
import org.koin.mp.KoinPlatform.getKoin

// Exposes shared, non-ViewModel Koin singletons to Swift - a counterpart to
// IosViewModels, kept separate since FeatureEventBus isn't a ViewModel.
object IosServices {
    fun featureEventBus(): FeatureEventBus = getKoin().get()
}
