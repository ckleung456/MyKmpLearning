package data

import android.content.Context
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { context.filesDir.resolve(DATA_STORE_FILE) }
    )
)

class AndroidStringEncryptor(context: Context) : StringEncryptor {
    companion object {
        private const val PREF_KEYS = "secure_prefs_keys"
        private const val KEY_SETS = "my_key_set"
        private const val AES256 = "AES256_GCM"
        private const val URI = "android-keystore://preference_master_key"
    }

    private val aead: Aead

    init {
        AeadConfig.register()
        val keySetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, PREF_KEYS, KEY_SETS)
            .withKeyTemplate(KeyTemplates.get(AES256))
            .withMasterKeyUri(URI)
            .build().keysetHandle
        aead = keySetHandle.getPrimitive(RegistryConfiguration.get() ,Aead::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    override fun encrypt(plainText: String): String {
        val encrypted = aead.encrypt(plainText.toByteArray(Charsets.UTF_8), null)
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    override fun decrypt(cipherText: String): String {
        val decoded = Base64.decode(cipherText, Base64.NO_WRAP)
        val decrypted = aead.decrypt(decoded, null)
        return String(decrypted, Charsets.UTF_8)
    }
}