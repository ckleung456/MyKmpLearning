import di.dataModule
import di.dataStoreModule
import di.databaseModule
import di.dbPlatformModule
import di.dispatchersModule
import di.networkModule
import model.di.featureRegistryModule
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
        modules(databaseModule, dbPlatformModule, dataModule, dataStoreModule)

        // feature modules
        modules(
            featureRegistryModule
        )
    }
}