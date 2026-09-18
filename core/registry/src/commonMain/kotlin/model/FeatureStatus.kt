package model

data class FeatureStatus(
    val featureId: String,
    val version: String,
    val availability: FeatureAvailability,
    val isInitialized: Boolean,
    val dependencies: List<String>
)
