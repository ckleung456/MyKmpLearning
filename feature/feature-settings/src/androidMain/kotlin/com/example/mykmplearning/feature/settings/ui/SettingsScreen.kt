package com.example.mykmplearning.feature.settings.ui

import UiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import model.domain.NavigationSettingItem
import model.domain.ToggleSettingItem
import model.ui.setting.SettingsAction
import model.ui.setting.SettingsState
import org.koin.compose.viewmodel.koinViewModel
import viewmodel.SettingsViewModel

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: UiState<SettingsState>,
    onAction: (SettingsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (state is UiState.Success) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(
                    items = state.data.items,
                    key = { it.id }
                ) { item ->
                    when (item) {
                        is ToggleSettingItem -> {
                            ListItem(
                                headlineContent = { Text(item.label) },
                                trailingContent = {
                                    Switch(
                                        checked = item.checked,
                                        onCheckedChange = { checked ->
                                            onAction(SettingsAction.OnToggleChanged(item.id, checked))
                                        }
                                    )
                                }
                            )
                        }
                        is NavigationSettingItem -> {
                            ListItem(
                                modifier = Modifier.clickable {
                                    onAction(SettingsAction.OnNavigationItemClick(item.id))
                                },
                                headlineContent = { Text(item.label) },
                                trailingContent = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(
            state = UiState.Success(
                SettingsState(
                    items = persistentListOf(
                        ToggleSettingItem(id = "notifications", label = "Notifications", checked = true),
                        ToggleSettingItem(id = "dark_mode", label = "Dark Mode", checked = false),
                        NavigationSettingItem(id = "about", label = "About")
                    )
                )
            ),
            onAction = {}
        )
    }
}
