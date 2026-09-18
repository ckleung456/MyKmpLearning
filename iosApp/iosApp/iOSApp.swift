import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        initKoin()
        IosFeatureBootstrap.shared.start()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
