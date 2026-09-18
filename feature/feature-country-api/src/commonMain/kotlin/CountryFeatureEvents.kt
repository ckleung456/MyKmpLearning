import model.FeatureEvent

/**
 * Published on the shared FeatureEventBus to ask the app shell to navigate
 * into the Countries feature. See FeatureEventBus for the general
 * cross-feature communication pattern this follows.
 */
data object OpenCountriesEvent : FeatureEvent
