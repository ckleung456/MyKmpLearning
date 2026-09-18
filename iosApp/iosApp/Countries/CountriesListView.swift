import SwiftUI
import Shared

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
