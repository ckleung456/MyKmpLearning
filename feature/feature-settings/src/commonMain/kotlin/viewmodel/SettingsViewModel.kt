package viewmodel

import CountryFeatureApi
import FeatureEventBus
import FeatureRegistry
import OpenCountriesEvent
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import model.domain.MockSettingsData
import model.domain.NavigationSettingItem
import model.domain.ToggleSettingItem
import model.ui.setting.SettingsAction
import model.ui.setting.SettingsState

class SettingsViewModel(
    private val featureRegistry: FeatureRegistry,
    private val featureEventBus: FeatureEventBus
) : ViewModel() {

    private val countryFeatureVersion: String by lazy {
        featureRegistry.getFeature<CountryFeatureApi>()?.version.orEmpty()
    }

    private val _state = MutableStateFlow<UiState<SettingsState>>(UiState.Loading)
    val state = _state
        .onStart {
            MockSettingsData.initial().toMutableList().map { item ->
                when (item.id) {
                    MockSettingsData.OPEN_COUNTRIES_ID -> NavigationSettingItem(id = MockSettingsData.OPEN_COUNTRIES_ID, label = "Browse Countries, version: $countryFeatureVersion")
                    else -> item
                }
            }.toImmutableList().let { newItems ->
                _state.update {
                    UiState.Success(SettingsState(newItems))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnToggleChanged -> {
                _state.update { current ->
                    if (current !is UiState.Success) return@update current
                    val updatedItems = current.data.items.map { item ->
                        if (item.id == action.id && item is ToggleSettingItem) {
                            item.copy(checked = action.checked)
                        } else {
                            item
                        }
                    }
                    UiState.Success(current.data.copy(items = updatedItems.toImmutableList()))
                }
            }
            is SettingsAction.OnNavigationItemClick -> {
                if (action.id == MockSettingsData.OPEN_COUNTRIES_ID) {
                    featureEventBus.publish(OpenCountriesEvent)
                }
            }
        }
    }
}
