import SwiftUI
import Shared

// NOTE: assumes SKIE's default flattened naming for nested Kotlin sealed
// types (e.g. SettingsAction.OnToggleChanged -> SettingsActionOnToggleChanged).
// Verify against the generated Shared module in Xcode.
struct SettingsView: View {
    private let viewModel = IosViewModels.shared.settings()
    @State private var state: UiState = UiStateLoading.shared

    var body: some View {
        Group {
            if let success = state as? UiStateSuccess<SettingsState> {
                List(Array(success.data.items.enumerated()), id: \.offset) { _, item in
                    if let toggle = item as? ToggleSettingItem {
                        Toggle(toggle.label, isOn: Binding(
                            get: { toggle.checked },
                            set: { newValue in
                                viewModel.onAction(
                                    action: SettingsActionOnToggleChanged(id: toggle.id, checked: newValue)
                                )
                            }
                        ))
                    } else if let navigation = item as? NavigationSettingItem {
                        HStack {
                            Text(navigation.label)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .foregroundStyle(.secondary)
                        }
                    }
                }
            } else {
                ProgressView()
            }
        }
        .navigationTitle("Settings")
        .task {
            for await value in viewModel.state {
                state = value
            }
        }
    }
}
