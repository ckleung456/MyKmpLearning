package model.ui.countries

sealed interface CountriesEvent {
    data class NavigateToDetail(val code: String) : CountriesEvent
}