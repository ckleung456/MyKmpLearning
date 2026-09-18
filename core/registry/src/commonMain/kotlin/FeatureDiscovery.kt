import model.FeatureApi
import model.FeatureAvailability
import model.FeatureStatus
import model.RouteAwareFeature

class FeatureDiscovery(
    val registry: FeatureRegistry
) {

    /**
     * Discover all features that implement a specific interface
     */
    inline fun <reified T: FeatureApi> discoverFeatures(): List<T> = registry.getFeatures<T>()

    /**
     * Find features that depend on on given feature
     */
    fun getFeatureDependingOn(featureId: String) = registry.registeredFeatures.values
        .filter { feature -> feature.dependencies.contains(featureId) }

    /**
     * Get feature by navigation route
     */
    fun getFeatureForRoute(route: String): FeatureApi? = registry.registeredFeatures.values
        .firstOrNull { featureApi -> featureApi is RouteAwareFeature && featureApi.canHandleRoute(route) }

    /**
     * Get all available features with their status
     */
    fun getFeaturesStatus(): Map<String, FeatureStatus> = registry.registeredFeatures.mapValues { (id, featureApi) ->
        FeatureStatus(
            featureId = id,
            version = featureApi.version,
            availability = featureApi.availability,
            isInitialized = true,
            dependencies = featureApi.dependencies
        )
    }
}
