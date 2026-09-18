package di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}

val dispatchersModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<CoroutineDispatcher>(qualifier = named("main")) { Dispatchers.Main }
    single<CoroutineDispatcher>(qualifier = named("default")) { Dispatchers.Default }
    single<CoroutineDispatcher>(qualifier = named("defaultLimit")) { Dispatchers.Default.limitedParallelism(1) }
}
