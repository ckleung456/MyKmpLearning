package viewmodel

import UiState
import UseCaseOutputWithStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.ui.CountryDetailUi
import model.ui.country.CountryDetailState
import toDisplayMessage
import usecase.GetCountryDetailUseCase

class CountryDetailViewModel(
    private val code: String,
    private val getCountryDetailUseCase: GetCountryDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<CountryDetailState>>(UiState.Loading)
    val state = _state
        .onStart {
            loadDetail()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState.Loading
        )

    private fun loadDetail() {
        viewModelScope.launch {
            getCountryDetailUseCase.invoke(code).collect { result ->
                when (result) {
                    is UseCaseOutputWithStatus.Progress -> {
                        _state.update { UiState.Loading }
                    }
                    is UseCaseOutputWithStatus.Success -> {
                        _state.update {
                            UiState.Success(CountryDetailState(detail = result.result))
                        }
                    }
                    is UseCaseOutputWithStatus.Failed -> {
                        _state.update {
                            UiState.Error(message = result.error.toDisplayMessage())
                        }
                    }
                }
            }
        }
    }
}
