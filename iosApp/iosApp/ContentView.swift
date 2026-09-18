import SwiftUI
import Shared

struct ContentView: View {
    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            NavigationStack {
                CountriesListView()
            }
            .tabItem {
                Label("Countries", systemImage: "globe")
            }
            .tag(0)

            NavigationStack {
                SettingsView()
            }
            .tabItem {
                Label("Settings", systemImage: "gearshape")
            }
            .tag(1)
        }
        .task {
            for await event in IosServices.shared.featureEventBus().events {
                if event is OpenCountriesEvent {
                    selectedTab = 0
                }
            }
        }
    }
}
