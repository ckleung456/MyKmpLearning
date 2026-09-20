import SwiftUI
import CryptoKit
import Security
import Shared

// Keychain-backed persistence for the DataStore encryption key - AES.GCM needs
// the *same* key for every encrypt/decrypt call, across app launches, which a
// freshly-generated SymmetricKey per call (or per session) can never provide.
private enum SecurePrefsKeychain {
    private static let service = "com.example.mykmplearning.securePrefs"
    private static let account = "dataStoreEncryptionKey"

    static func loadOrCreateKey() -> SymmetricKey {
        if let existing = read() {
            return existing
        }
        let created = SymmetricKey(size: .bits256)
        save(created)
        return created
    }

    private static func read() -> SymmetricKey? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        var item: CFTypeRef?
        guard SecItemCopyMatching(query as CFDictionary, &item) == errSecSuccess,
              let data = item as? Data else {
            return nil
        }
        return SymmetricKey(data: data)
    }

    private static func save(_ key: SymmetricKey) {
        let keyData = key.withUnsafeBytes { Data($0) }
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]
        SecItemDelete(query as CFDictionary)
        var attributes = query
        attributes[kSecValueData as String] = keyData
        attributes[kSecAttrAccessible as String] = kSecAttrAccessibleAfterFirstUnlock
        SecItemAdd(attributes as CFDictionary, nil)
    }
}

@main
struct iOSApp: App {
    init() {
        let encryptionKey = SecurePrefsKeychain.loadOrCreateKey()

        EncryptionHandler.shared.encryptionCallback = { plainText in
            guard let data = plainText.data(using: .utf8),
                  let sealedBox = try? AES.GCM.seal(data, using: encryptionKey),
                  let combined = sealedBox.combined else {
                return ""
            }
            return combined.base64EncodedString()
        }

        EncryptionHandler.shared.decryptionCallback = { cipherText in
            guard let combined = Data(base64Encoded: cipherText),
                  let sealedBox = try? AES.GCM.SealedBox(combined: combined),
                  let decryptedData = try? AES.GCM.open(sealedBox, using: encryptionKey),
                  let plainText = String(data: decryptedData, encoding: .utf8) else {
                return ""
            }
            return plainText
        }

        initKoin()
        IosFeatureBootstrap.shared.start()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
