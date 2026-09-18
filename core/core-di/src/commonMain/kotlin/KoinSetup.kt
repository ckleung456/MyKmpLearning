import di.dispatchersModule
import di.networkModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(
    platformBlock: KoinApplication.() -> Unit = {}
): KoinApplication {
    return startKoin {
        platformBlock()

        // core modules (network, dispatchers)
        modules(networkModule)
        modules(dispatchersModule)

        // feature modules
        modules(
            featureRegistryModule
        )
    }
}