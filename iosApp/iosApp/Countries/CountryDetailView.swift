import SwiftUI
import SKIE
import Shared

struct CountryDetailView: View {
    let code: String
    @State private var viewModel: CountryDetailViewModel? = nil
    @State private var state: UiState = UiStateLoading.shared

    var body: some View {
        Group {
            if let success = state as? UiStateSuccess<CountryDetailState>, let detail = success.data.detail {
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer().frame(height: 48)
                        Text(detail.flagEmoji)
                            .font(.system(size: 64))
                        Spacer().frame(height: 32)
                        VStack(spacing: 0) {
                            ForEach(Array(detail.fields.enumerated()), id: \.offset) { _, field in
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(field.label)
                                        .font(.caption)
                                        .foregroundStyle(.secondary)
                                    Text(field.value)
                                        .font(.body)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.horizontal, 16)
                                .padding(.vertical, 12)
                                Divider()
                            }
                        }
                    }
                }
            } else if let error = state as? UiStateError {
                Text(error.message)
                    .foregroundStyle(.secondary)
            } else {
                ProgressView()
            }
        }
        .navigationTitle("Country Details")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            let vm = IosViewModels.shared.countryDetail(code: code)
            viewModel = vm
            defer {
                vm.clear()
                viewModel = nil
            }
            for await value in vm.state {
                state = value
            }
        }
    }
}
