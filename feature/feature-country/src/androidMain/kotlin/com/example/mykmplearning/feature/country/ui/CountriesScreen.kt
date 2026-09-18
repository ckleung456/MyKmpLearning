package com.example.mykmplearning.feature.country.ui

import SimpleErrorView
import SimpleLoadingView
import UIStatefulContent
import UiState
import Utils.ObserveAsEvents
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import model.ui.CountryListItem
import model.ui.CountryUi
import model.ui.countries.CountriesAction
import model.ui.countries.CountriesEvent
import model.ui.countries.CountriesState
import org.koin.compose.viewmodel.koinViewModel
import viewmodel.CountriesViewModel

@Composable
fun CountriesRoot(
    onNavigateToDetail: (String) -> Unit,
    viewModel: CountriesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(flow = viewModel.events, key1 = viewModel) { event ->
        when (event) {
            is CountriesEvent.NavigateToDetail -> onNavigateToDetail(event.code)
        }
    }

    CountriesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesScreen(
    state: UiState<CountriesState>,
    onAction: (CountriesAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Countries") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        UIStatefulContent(
            state = state,
            loadingContent = {
                SimpleLoadingView(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            },
            errorContent = { errorMessage, _ ->
                SimpleErrorView(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    errorMessage = errorMessage,
                    showRetry = true,
                    onRetry = {
                        onAction(CountriesAction.OnFetchCountries)
                    }
                )
            },
            successContent = { data ->
                CountriesListView(
                    modifier = Modifier.padding(innerPadding),
                    countries = data.countries,
                    onSelectedCountry = { code ->
                        onAction(CountriesAction.OnCountryClick(code = code))
                    }
                )
            }
        )
    }
}

@Composable
private fun CountriesListView(
    modifier: Modifier,
    countries: List<CountryListItem>,
    onSelectedCountry: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = countries,
            key = { item ->
                when (item) {
                    is CountryListItem.Header -> "header_${item.letter}"
                    is CountryListItem.CountryRow -> item.country.code
                }
            },
            contentType = { item ->
                when (item) {
                    is CountryListItem.Header -> "header"
                    is CountryListItem.CountryRow -> "country"
                }
            }
        ) { item ->
            when (item) {
                is CountryListItem.Header -> CountryLetterHeader(letter = item.letter)
                is CountryListItem.CountryRow -> CountryRow(
                    country = item.country,
                    onClick = { onSelectedCountry(item.country.code) }
                )
            }
        }
    }
}

@Composable
private fun CountryLetterHeader(letter: String) {
    Text(
        text = letter,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun CountryRow(country: CountryUi, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Text(text = country.flagEmoji, fontSize = 32.sp)
        },
        headlineContent = { Text(country.name) },
        supportingContent = { Text(country.code) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
private fun CountriesScreenPreview() {
    MaterialTheme {
        CountriesScreen(
            state = UiState.Success(
                CountriesState(
                    countries = persistentListOf(
                        CountryListItem.Header(letter = "K"),
                        CountryListItem.CountryRow(
                            CountryUi(code = "AF", name = "Afghanistan", flagEmoji = "🇦🇫")
                        ),
                        CountryListItem.Header(letter = "M"),
                        CountryListItem.CountryRow(
                            CountryUi(code = "AX", name = "Åland Islands", flagEmoji = "🇦🇽")
                        )
                    )
                )
            ),
            onAction = {}
        )
    }
}
