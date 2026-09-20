import io.ktor.client.engine.HttpClientEngineFactory

expect fun createPlatformEngine(): HttpClientEngineFactory<*>