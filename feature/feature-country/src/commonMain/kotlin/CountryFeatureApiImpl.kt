import kotlinx.coroutines.runBlocking
import model.FeatureAvailability
import model.FeatureDao

class CountryFeatureApiImpl(
    private val featureDao: FeatureDao
) : CountryFeatureApi {
    private val featureData by lazy {
        runBlocking {
            featureDao.getFeatureById(featureId = featureId)
        }
    }

    private val allowRouteSet = setOf(
        "SettingsRoute"
    )

    override val version: String
        get() = featureData?.version ?: "1.0.0"

    override val availability: FeatureAvailability
        get() = when(featureData?.availability) {
            1 -> FeatureAvailability.UNAVAILABLE
            2 -> FeatureAvailability.RESTRICTED_REGION
            3 -> FeatureAvailability.UNDER_MAINTENANCE
            4 -> FeatureAvailability.DEPRECATED
            5 -> FeatureAvailability.EXPERIMENTAL
            else -> FeatureAvailability.AVAILABLE
        }

    override fun canHandleRoute(route: String): Boolean = route in allowRouteSet
}
