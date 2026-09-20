package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import data.AppDatabase
import data.IosStringEncryptor
import data.StringEncryptor
import data.createDataStore
import data.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dbPlatformModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}

actual val dataModule: Module = module {
    single<DataStore<Preferences>> { createDataStore() }
    single<StringEncryptor> { IosStringEncryptor() }
}