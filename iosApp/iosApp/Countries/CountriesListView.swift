import SwiftUI
import Shared

private extension CountryListItem {
    var stableId: String {
        if let header = self as? CountryListItemHeader {
            return "header_\(header.letter)"
        } else if let row = self as? CountryListItemCountryRow {
            return row.country.code
        }
        return "unknown"
    }
}

struct CountriesListView: View {
    private let viewModel = IosViewModels.shared.countries()
    @State private var state: UiState = UiStateLoading.shared

    var body: some View {
        Group {
            if let success = state as? UiStateSuccess<CountriesState> {
                List(success.data.countries, id: \.stableId) { item in
                    if let header = item as? CountryListItemHeader {
                        Text(header.letter)
                            .font(.headline)
                            .foregroundStyle(.secondary)
                    } else if let row = item as? CountryListItemCountryRow {
                        NavigationLink(value: row.country.code) {
                            HStack(spacing: 12) {
                                Text(row.country.flagEmoji)
                                    .font(.system(size: 32))
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(row.country.name)
                                    Text(row.country.code)
                                        .font(.caption)
                                        .foregroundStyle(.secondary)
                                }
                            }
                            .padding(.vertical, 4)
                        }
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
