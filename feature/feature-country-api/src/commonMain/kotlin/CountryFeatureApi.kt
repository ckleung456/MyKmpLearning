import model.FeatureApi
import model.RouteAwareFeature

interface CountryFeatureApi: FeatureApi, RouteAwareFeature {
    override val featureId: String
        get() = "CountryFeatureApi"

    override val dependencies: List<String>
        get() = emptyList()
}
