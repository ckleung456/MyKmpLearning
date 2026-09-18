import SwiftUI
import Shared

// NOTE: This assumes SKIE 0.10.14's default bridging - Kotlin StateFlow as an
// AsyncSequence (`for await`), Kotlin sealed classes exported with flattened
// names (UiState.Success -> UiStateSuccess), and Kotlin List/ImmutableList as
// a Swift Array. Verify against the generated Shared module in Xcode and
// adjust if the real generated API differs.
struct CountriesListView: View {
    private let viewModel = IosViewModels.shared.countries()
    @State private var state: UiState = UiStateLoading.shared

    var body: some View {
        Group {
            if let success = state as? UiStateSuccess<CountriesState> {
                List(success.data.countries, id: \.code) { country in
                    NavigationLink(value: country.code) {
                        HStack(spacing: 12) {
                            Text(country.flagEmoji)
                                .font(.system(size: 32))
                            VStack(alignment: .leading, spacing: 2) {
                                Text(country.name)
                                Text(country.code)
                                    .font(.caption)
                                    .foregroundStyle(.secondary)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
                .listStyle(.plain)
            } else if let error = state as? UiStateError {
                Text(error.message)
                    .foregroundStyle(.secondary)
            } else {
                ProgressView()
            }
        }
        .navigationTitle("Countries")
        .navigationDestination(for: String.self) { code in
            CountryDetailView(code: code)
        }
        .task {
            for await value in viewModel.state {
                state = value
            }
        }
    }
}
