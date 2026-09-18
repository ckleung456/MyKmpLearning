package com.example.mykmplearning.feature.country.ui

import UiState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import model.ui.CountryDetailUi
import model.ui.DetailField
import model.ui.country.CountryDetailState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import viewmodel.CountryDetailViewModel

@Composable
fun CountryDetailRoot(
    code: String,
    onBack: () -> Unit,
    viewModel: CountryDetailViewModel = koinViewModel(parameters = { parametersOf(code) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CountryDetailScreen(
        state = state,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    state: UiState<CountryDetailState>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Country Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message)
                }
            }
            is UiState.Success -> {
                val detail = state.data.detail
                if (detail != null) {
                    CountryDetailContent(
                        detail = detail,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryDetailContent(
    detail: CountryDetailUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(48.dp))
        Text(
            text = detail.flagEmoji,
            fontSize = 64.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        detail.fields.forEachIndexed { index, field ->
            DetailFieldRow(field)
            if (index != detail.fields.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun DetailFieldRow(field: DetailField) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = field.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = field.value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun CountryDetailScreenPreview() {
    MaterialTheme {
        CountryDetailScreen(
            state = UiState.Success(
                CountryDetailState(
                    detail = CountryDetailUi(
                        flagEmoji = "🇦🇫",
                        fields = persistentListOf(
                            DetailField("Name", "Afghanistan"),
                            DetailField("Code", "AF"),
                            DetailField("Capital", "Kabul"),
                            DetailField("Region", "AS"),
                            DetailField("Currency", "Afghan afghani (AFN, ؋)"),
                            DetailField("Language", "Pashto (ps)")
                        )
                    )
                )
            ),
            onBack = {}
        )
    }
}
