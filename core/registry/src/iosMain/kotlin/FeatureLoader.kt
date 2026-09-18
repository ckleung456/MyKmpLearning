import model.FeatureApi

actual class FeatureLoader actual constructor() {
    actual suspend fun loadFeature(location: String, featureId: String): FeatureApi? {
        // iOS does not support dynamic DEX loading.
        // Features must be linked statically at compile time.
        return null
    }
}