package model.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.CountriesViewModel
import viewmodel.CountryDetailViewModel

val countriesPresentationModule = module {
    viewModelOf(::CountriesViewModel)
}

val countryPresentationModule = module {
    viewModel { params ->
        CountryDetailViewModel(
            code = params.get(),
            getCountryDetailUseCase = get()
        )
    }
}
