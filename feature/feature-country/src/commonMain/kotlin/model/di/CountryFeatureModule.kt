package model.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import repository.CountryRepository
import repository.CountryRepositoryImpl
import usecase.GetCountriesUseCase
import usecase.GetCountryDetailUseCase
import usecase.SearchCountriesUseCase
import viewmodel.CountriesViewModel
import viewmodel.CountryDetailViewModel

internal val countryFeaturePresentationModule = module {
    viewModelOf(::CountriesViewModel)
    viewModel { params ->
        CountryDetailViewModel(
            code = params.get(),
            getCountryDetailUseCase = get()
        )
    }
}

internal val countryFeatureDataModule = module {
    single<CountryRepository> {
        CountryRepositoryImpl(
            client = get(),
            ioDispatcher = get()
        )
    }
    single<GetCountriesUseCase> {
        GetCountriesUseCase(repository = get())
    }
    single<GetCountryDetailUseCase> {
        GetCountryDetailUseCase(repository = get())
    }
    single<SearchCountriesUseCase> { SearchCountriesUseCase() }
}

