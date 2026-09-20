package model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Assume there is a remote feature flags API which could update and/or
 *  controls these data
 */
@Entity(tableName = "features")
data class FeatureEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val featureId: String,
    val availability: Int,
    val version: String
)