package repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import model.dto.Country
import model.network.APIConstants
import org.koin.dsl.module

val countryRepositoryModule = module {
    single<CountryRepository> {
        CountryRepositoryImpl(
            client = get(),
            ioDispatcher = get()
        )
    }
}

interface CountryRepository {
    suspend fun getCountries(): Flow<List<Country>>

    suspend fun getCountry(code: String): Country?
}

class CountryRepositoryImpl(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher
) : CountryRepository {
    private val _countriesFLow = MutableStateFlow<List<Country>>(emptyList())
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getCountries(): Flow<List<Country>> = withContext(ioDispatcher) {
        val bodyText = client.get(APIConstants.GET_COUNTRY_API_URI).bodyAsText()
        val response = json.decodeFromString<List<Country>>(bodyText)
        _countriesFLow.tryEmit(response)
        _countriesFLow
    }

    override suspend fun getCountry(code: String): Country? {
        return _countriesFLow.value.find { it.code == code }
    }
}