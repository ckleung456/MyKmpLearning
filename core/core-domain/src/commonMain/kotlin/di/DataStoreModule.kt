package di

import data.SecureDataStore
import org.koin.dsl.module

val dataStoreModule = module {
    single<SecureDataStore> {
        SecureDataStore(
            dataStore = get(),
            encryptor = get()
        )
    }
}