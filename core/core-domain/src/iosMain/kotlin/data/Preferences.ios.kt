package data

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask


@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> = createDataStore(
    storage = OkioStorage(
        fileSystem = FileSystem.SYSTEM,
        serializer = PreferencesSerializer,
        producePath = {
            val dir = NSFileManager.defaultManager.URLForDirectory(
                NSDocumentDirectory, NSUserDomainMask, null, false, null
            )
            (requireNotNull(dir).path + "/${DATA_STORE_FILE}").toPath()
        }
    )
)

object EncryptionHandler {
    var encryptionCallback: ((String) -> String)? = null
    var decryptionCallback: ((String) -> String)? = null
}

class IosStringEncryptor : StringEncryptor {
    override fun encrypt(plainText: String): String = EncryptionHandler.encryptionCallback?.invoke(plainText) ?:
        throw IllegalStateException("Encryption not injected")

    override fun decrypt(ciphertext: String): String = EncryptionHandler.decryptionCallback?.invoke(ciphertext) ?:
        throw IllegalStateException("Decryption not injected")
}