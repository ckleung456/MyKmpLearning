package viewmodel

import FeatureEventBus
import OpenCountriesEvent
import UiState
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import model.domain.MockSettingsData
import model.domain.ToggleSettingItem
import model.ui.setting.SettingsAction
import model.ui.setting.SettingsState

class SettingsViewModel(
    private val featureEventBus: FeatureEventBus
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<SettingsState>>(
        UiState.Success(SettingsState(items = MockSettingsData.initial()))
    )
    val state = _state.asStateFlow()

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
