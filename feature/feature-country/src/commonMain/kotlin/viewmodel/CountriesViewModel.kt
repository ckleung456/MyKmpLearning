package viewmodel

import UiState
import UseCaseOutputWithStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.ui.countries.CountriesAction
import model.ui.countries.CountriesEvent
import model.ui.countries.CountriesState
import usecase.GetCountriesUseCase
import toDisplayMessage

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<CountriesState>>(UiState.Loading)
    val state = _state
        .onStart {
            loadCountries()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState.Loading
        )

    private val _events = Channel<CountriesEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CountriesAction) {
        when (action) {
            is CountriesAction.OnCountryClick -> {
                viewModelScope.launch {
                    _events.send(CountriesEvent.NavigateToDetail(action.code))
                }
            }
            is CountriesAction.OnFetchCountries -> loadCountries()
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            getCountriesUseCase.invoke(Unit).collect { result ->
                when (result) {
                    is UseCaseOutputWithStatus.Progress -> {
                        _state.update { UiState.Loading }
                    }
                    is UseCaseOutputWithStatus.Success -> {
                        _state.update {
                            UiState.Success(CountriesState(countries = result.result.items))
                        }
                    }
                    is UseCaseOutputWithStatus.Failed -> {
                        _state.update {
                            UiState.Error(
                                message = result.error.toDisplayMessage(),
                                exception = result.error
                            )
                        }
                    }
                }
            }
        }
    }
}
