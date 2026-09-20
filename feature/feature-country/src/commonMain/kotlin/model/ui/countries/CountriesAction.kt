package model.ui.countries

sealed interface CountriesAction {
    data class OnCountryClick(val code: String) : CountriesAction

    object OnFetchCountries : CountriesAction

    object OnSearchClick : CountriesAction

    data class OnSearchQueryChange(val query: String) : CountriesAction

    object OnCloseSearchClick : CountriesAction
}