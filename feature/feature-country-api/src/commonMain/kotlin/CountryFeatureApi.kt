import model.FeatureApi
import model.FeatureAvailability

interface CountryFeatureApi: FeatureApi {
    override val featureId: String
        get() = "CountryFeatureApi"

    override val availability: FeatureAvailability
        get() = FeatureAvailability.AVAILABLE

    override val version: String
        get() = "1.0.0"

    override val dependencies: List<String>
        get() = emptyList()
}
