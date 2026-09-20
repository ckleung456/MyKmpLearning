package model.di

import CountryFeatureApi
import CountryFeatureApiImpl
import model.network.CountryFeatureConstant.COUNTRY_FEATURE_SCOPE
import org.koin.core.qualifier.named
import org.koin.dsl.module
import repository.CountryRepository
import repository.CountryRepositoryImpl
import usecase.GetCountriesUseCase
import usecase.GetCountryDetailUseCase
import usecase.SearchCountriesUseCase
import viewmodel.CountriesViewModel
import viewmodel.CountryDetailViewModel

internal val countryFeaturePresentationModule = module {
    scope(named(COUNTRY_FEATURE_SCOPE)) {
        scoped {
            CountriesViewModel(
                getCountriesUseCase = get(),
                searchCountriesUseCase = get()
            )
        }
        scoped { params ->
            CountryDetailViewModel(
                code = params.get(),
                getCountryDetailUseCase = get()
            )
        }
    }
}

internal val countryFeatureDataModule = module {
    scope(named(COUNTRY_FEATURE_SCOPE)) {
        scoped<CountryRepository> {
            CountryRepositoryImpl(
                client = get(),
                ioDispatcher = get()
            )
        }
        scoped<GetCountriesUseCase> {
            GetCountriesUseCase(repository = get())
        }
        scoped<GetCountryDetailUseCase> {
            GetCountryDetailUseCase(repository = get())
        }
        scoped<SearchCountriesUseCase> { SearchCountriesUseCase() }
    }
}

val countryFeatureApiModule = module {
    single<CountryFeatureApi> {
        CountryFeatureApiImpl(featureDao = get())
    }
    includes(
        countryFeaturePresentationModule,
        countryFeatureDataModule
    )
}
