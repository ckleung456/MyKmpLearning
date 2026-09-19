import kotlin.jvm.JvmName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.FeatureApi
import model.FeatureAvailability

class FeatureRegistry {
    // thread-safe access via Mutex
    private val mutex = Mutex()

    private val _registeredFeatures = mutableMapOf<String, FeatureApi>()
    val registeredFeatures: Map<String, FeatureApi>
        get() = _registeredFeatures.toMap()

    private val _featureAvailability = MutableStateFlow<Map<String, FeatureAvailability>>(emptyMap())
    val featureAvailability = _featureAvailability.asStateFlow()

    /**
     * Register a feature dynamically at runtime
     */
    suspend fun <T: FeatureApi> registerFeature(feature: T) {
        val id = feature.featureId
        mutex.withLock {
            _registeredFeatures[id] = feature
            _featureAvailability.update { it + (id to feature.availability) }
        }
    }

    /**
     * unregister a feature
     */
    suspend fun unregisterFeature(featureId: String) {
        mutex.withLock {
            _registeredFeatures.remove(featureId)
            _featureAvailability.update { it - featureId }
        }
    }

    /**
     * Get a feature by ID with type safety
     */
    @JvmName("getFeatureOfType")
    inline fun <reified T: FeatureApi> getFeature(featureId: String): T? = getFeature(featureId) as T?

    fun getFeature(featureId: String): FeatureApi? = _registeredFeatures[featureId]?.takeIf { it.isAvailable() }

    /**
     * Get feature by class when only know the interface
     */
    inline fun <reified T: FeatureApi> getFeature(): T? = registeredFeatures.values
        .filterIsInstance<T>()
        .firstOrNull { it.isAvailable() }

    /**
     * check if feature is available
     */
    fun isFeatureAvailable(featureId: String): Boolean = registeredFeatures[featureId]?.isAvailable() ?: false

    /**
     * Get all features of certain type
     */
    inline fun <reified T: FeatureApi> getFeatures(): List<T> = registeredFeatures.values.filterIsInstance<T>()
}