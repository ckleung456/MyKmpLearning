package model.ui.countries

sealed interface CountriesAction {
    data class OnCountryClick(val code: String) : CountriesAction
}