package model

import org.koin.core.module.Module

interface FeatureApi {
    val featureId: String
    val availability: FeatureAvailability
    val version: String
    val dependencies: List<String>  // Feature IDs this depends on

    val featureModule: Module

    fun isAvailable(): Boolean = availability == FeatureAvailability.AVAILABLE
    fun initialize() {}  // Called when feature is loaded
    fun cleanup() {}     // Called when feature is unloaded
}

enum class FeatureAvailability {
    AVAILABLE,
    UNAVAILABLE,
    RESTRICTED_REGION,
    UNDER_MAINTENANCE,
    DEPRECATED,
    EXPERIMENTAL
}