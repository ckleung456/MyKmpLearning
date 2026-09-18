import org.koin.dsl.module

val featureRegistryModule = module {
    single<FeatureRegistry> { FeatureRegistry() }
    single<FeatureDiscovery> { FeatureDiscovery(registry = get()) }
    single<FeatureEventBus> { FeatureEventBus() }
}