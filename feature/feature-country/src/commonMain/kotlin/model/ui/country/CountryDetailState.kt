package model.ui.country

import androidx.compose.runtime.Stable
import model.ui.CountryDetailUi

@Stable
data class CountryDetailState(
    val detail: CountryDetailUi? = null
)
