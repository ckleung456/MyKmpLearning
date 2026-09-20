package viewmodel

import UiState
import UseCaseOutputWithStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.ui.CountryUi
import model.ui.countries.CountriesAction
import model.ui.countries.CountriesEvent
import model.ui.countries.CountriesState
import model.ui.toGroupedCountryListItems
import usecase.GetCountriesUseCase
import toDisplayMessage
import usecase.SearchCountriesUseCase
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
    }

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

    private val _query = MutableStateFlow("")
    private var cachedCountries: List<CountryUi> = emptyList()

    init {
        viewModelScope.launch {
            _query.debounce(SEARCH_DEBOUNCE_MILLIS.milliseconds).distinctUntilChanged().collect { query ->
                applyFilter(query)
            }
        }
    }

    fun onAction(action: CountriesAction) {
        when (action) {
            is CountriesAction.OnCountryClick -> {
                viewModelScope.launch {
                    _events.send(CountriesEvent.NavigateToDetail(action.code))
                }
            }
            is CountriesAction.OnFetchCountries -> loadCountries()
            is CountriesAction.OnSearchClick -> {
                _state.update { current ->
                    if (current is UiState.Success) {
                        current.copy(data = current.data.copy(isSearching = true))
                    } else {
                        current
                    }
                }
            }
            is CountriesAction.OnSearchQueryChange -> {
                _state.update { current ->
                    if (current is UiState.Success) {
                        current.copy(data = current.data.copy(query = action.query))
                    } else {
                        current
                    }
                }
                _query.value = action.query
            }
            is CountriesAction.OnCloseSearchClick -> {
                _state.update { current ->
                    if (current !is UiState.Success) return@update current
                    if (current.data.query.isNotBlank()) {
                        current.copy(
                            data = current.data.copy(
                                query = "",
                                countries = cachedCountries.toGroupedCountryListItems()
                            )
                        )
                    } else {
                        current.copy(data = current.data.copy(isSearching = false))
                    }
                }
                _query.value = ""
            }
        }
    }

    private fun applyFilter(query: String) {
        if (state.value !is UiState.Success) return
        viewModelScope.launch {
            searchCountriesUseCase.invoke(
                input = SearchCountriesUseCase.Input(
                    query = query,
                    countries = cachedCountries
                )
            ).collect { state ->
                if (state is UseCaseOutputWithStatus.Success) {
                    _state.update { current ->
                        if (current !is UiState.Success) return@update current
                        current.copy(data = current.data.copy(countries = state.result))
                    }
                }
            }
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
                        cachedCountries = result.result.allCountries
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
