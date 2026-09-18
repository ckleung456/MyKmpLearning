import model.FeatureApi

expect class FeatureLoader() {
    suspend fun loadFeature(location: String, featureId: String): FeatureApi?
}