package model

import androidx.room.TypeConverter

class FeatureAvailabilityConverter {
    @TypeConverter
    fun fromAvailability(availability: FeatureAvailability): Int = availability.id

    @TypeConverter
    fun toAvailability(raw: Int) = when (raw) {
        0 -> FeatureAvailability.AVAILABLE
        1 -> FeatureAvailability.UNAVAILABLE
        2 -> FeatureAvailability.RESTRICTED_REGION
        3 -> FeatureAvailability.UNDER_MAINTENANCE
        4 -> FeatureAvailability.DEPRECATED
        5 -> FeatureAvailability.EXPERIMENTAL
        else -> FeatureAvailability.UNAVAILABLE
    }
}