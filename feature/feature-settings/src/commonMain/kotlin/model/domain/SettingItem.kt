package model.domain

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface SettingItem {
    val id: String
    val label: String
}

@Stable
data class ToggleSettingItem(
    override val id: String,
    override val label: String,
    val checked: Boolean
) : SettingItem

@Stable
data class NavigationSettingItem(
    override val id: String,
    override val label: String
) : SettingItem

object MockSettingsData {
    const val OPEN_COUNTRIES_ID = "open_countries"

    fun initial(): ImmutableList<SettingItem> = persistentListOf(
        ToggleSettingItem(id = "notifications", label = "Notifications", checked = true),
        ToggleSettingItem(id = "dark_mode", label = "Dark Mode", checked = false),
        ToggleSettingItem(id = "auto_sync", label = "Auto-Sync", checked = true),
        NavigationSettingItem(id = OPEN_COUNTRIES_ID, label = "Browse Countries"),
        NavigationSettingItem(id = "language", label = "Language"),
        NavigationSettingItem(id = "about", label = "About"),
        NavigationSettingItem(id = "privacy_policy", label = "Privacy Policy")
    )
}
