package data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import model.FeatureAvailabilityConverter
import model.FeatureDao
import model.FeatureEntity

@Database(
    entities = [
        FeatureEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(FeatureAvailabilityConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun featureDao(): FeatureDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
