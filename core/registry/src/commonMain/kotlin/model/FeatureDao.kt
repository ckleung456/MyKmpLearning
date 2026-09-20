package model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeatureDao {
    @Insert
    suspend fun insertFeature(feature: FeatureEntity)

    @Delete
    suspend fun delete(feature: FeatureEntity)

    @Query("SELECT * FROM features WHERE featureId = :featureId")
    suspend fun getFeatureById(featureId: String): FeatureEntity?
}