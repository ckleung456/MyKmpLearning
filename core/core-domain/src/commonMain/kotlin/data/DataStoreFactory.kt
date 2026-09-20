package data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal const val DATA_STORE_FILE = "prefs.preferences_pb"

fun createDataStore(
    storage: Storage<Preferences>
): DataStore<Preferences> = DataStoreFactory.create(storage)

interface StringEncryptor {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}

class SecureDataStore(
    private val dataStore: DataStore<Preferences>,
    private val encryptor: StringEncryptor
) {
    fun getString(key: String, defaultValue: String = ""): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            val encryptedValue = preferences[prefKey] ?: return@map defaultValue
            try {
                encryptor.decrypt(encryptedValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        val encryptedValue = encryptor.encrypt(value)
        dataStore.edit { preferences ->
            preferences[prefKey] = encryptedValue
        }
    }
}