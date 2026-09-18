import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            NavigationStack {
                CountriesListView()
            }
            .tabItem {
                Label("Countries", systemImage: "globe")
            }

            NavigationStack {
                SettingsView()
            }
            .tabItem {
                Label("Settings", systemImage: "gearshape")
            }
        }
    }
}
