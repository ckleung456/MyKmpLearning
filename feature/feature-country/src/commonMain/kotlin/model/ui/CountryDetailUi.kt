package model.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CountryDetailUi(
    val flagEmoji: String,
    val fields: ImmutableList<DetailField>
)

@Stable
data class DetailField(
    val label: String,
    val value: String
)
