import kotlinx.coroutines.runBlocking
import model.FeatureAvailability
import model.FeatureDao

class SettingsFeatureApiImpl(featureDao: FeatureDao) : SettingsFeatureApi {
    private val featureData by lazy {
        runBlocking {
            featureDao.getFeatureById(featureId = featureId)
        }
    }

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
}
