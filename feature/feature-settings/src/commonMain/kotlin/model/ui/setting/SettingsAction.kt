package model.ui.setting

sealed interface SettingsAction {
    data class OnToggleChanged(val id: String, val checked: Boolean) : SettingsAction
    data class OnNavigationItemClick(val id: String) : SettingsAction
}
