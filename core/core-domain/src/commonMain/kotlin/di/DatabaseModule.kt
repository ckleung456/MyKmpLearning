package di

import androidx.room.RoomDatabase
import data.AppDatabase
import data.getRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { getRoomDatabase(get<RoomDatabase.Builder<AppDatabase>>()) }
    single { get<AppDatabase>().featureDao() }
}