package model

import org.koin.core.module.Module

interface FeatureApi {
    val featureId: String
    val availability: FeatureAvailability
    val version: String
    val dependencies: List<String>  // Feature IDs this depends on

    val featureModule: Module

    fun isAvailable(): Boolean = availability == FeatureAvailability.AVAILABLE
}

enum class FeatureAvailability(val id: Int) {
    AVAILABLE(0),
    UNAVAILABLE(1),
    RESTRICTED_REGION(2),
    UNDER_MAINTENANCE(3),
    DEPRECATED(4),
    EXPERIMENTAL(5)
}