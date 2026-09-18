package model.ui.setting

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import model.domain.SettingItem

@Stable
data class SettingsState(
    val items: ImmutableList<SettingItem>
)
