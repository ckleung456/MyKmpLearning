import model.FeatureApi

interface SettingsFeatureApi: FeatureApi {
    override val featureId: String
        get() = "SettingsFeatureApi"

    override val dependencies: List<String>
        get() = emptyList()
}
