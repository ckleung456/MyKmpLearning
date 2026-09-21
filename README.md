# MyKmpLearning

A Kotlin Multiplatform (KMP) sample app exploring how to structure a modular, multi-feature
mobile app that shares business logic between Android and iOS while keeping each platform's UI
fully native — Jetpack Compose on Android, SwiftUI on iOS. It's built as a learning/reference
project for KMP architecture patterns rather than a production app: a small "Countries" browser
(list + search + detail) and a "Settings" screen, wired together to demonstrate feature
modularization, cross-feature navigation, dependency injection, local persistence, and networking
in a multiplatform codebase.

## What it does

- **Countries**: fetches a country list over the network, groups it alphabetically, and lets you
  search by country name or capital (debounced, client-side filtering — no re-fetch per
  keystroke). Tapping a country opens a detail screen.
- **Settings**: a list of toggles and navigation rows, including a row that navigates to the
  Countries feature from Settings — demonstrating cross-feature communication between two
  independently-built feature modules.

Both screens run on Android and iOS from the same shared `ViewModel`s, `State`/`Action`/`Event`
models, and use cases — only the UI layer (Compose vs. SwiftUI) and a thin platform
implementation layer differ.

## How it's put together

### Targets

| Platform | UI | Status |
|---|---|---|
| Android | Jetpack Compose | Fully implemented |
| iOS | Native SwiftUI, consuming shared Kotlin `ViewModel`s via [SKIE](https://skie.touchlab.co/) | Fully implemented |
| Web (JS / Wasm) | Compose Multiplatform | Scaffolded by the KMP project wizard, not currently wired up — `webApp`'s entry point references an `App()` composable that doesn't exist in `shared` yet |

### Module layout

```
androidApp/            Android application entry point (MainActivity, bottom nav, DI bootstrap)
iosApp/                iOS application entry point (SwiftUI views, App entry, Xcode project)
webApp/                Web application entry point (js/wasmJs) — template scaffolding, inactive
shared/                Aggregator module: assembles the iOS "Shared.framework" and exports
                        the public API of the modules below into it (via export()/api()
                        in shared/build.gradle.kts)

core/
  core-network/         Ktor HttpClient setup, platform HTTP engines (OkHttp on Android, Darwin on iOS)
  core-domain/          Room (KMP) database, DataStore + encrypted preferences, generic
                         FlowUseCase base class, Koin-scoped ViewModel base class
  core-presentation/    Shared UI plumbing: UiState<T>, loading/error views, event-observing
                         Compose helpers, error-to-message mapping
  registry/             The feature registry: FeatureApi, FeatureRegistry, FeatureDiscovery,
                         FeatureEventBus, and a Room-backed feature-availability store

feature/
  feature-country/      Countries list, search, and detail screens + ViewModels/use cases
  feature-country-api/  Public contract for the Country feature (consumed by other features
                         without depending on its implementation)
  feature-settings/     Settings screen + ViewModel
  feature-settings-api/ Public contract for the Settings feature
```

**Dependency direction is one-way**: `core/*` modules are leaves with no dependencies on
features; `feature/*` modules depend on `core/*` and on other features' `-api` modules only
(never on another feature's implementation module); `shared` and `androidApp` sit at the top and
depend on everything below them, never the reverse. A `feature-x` / `feature-x-api` split lets
one feature call into another's public surface without a hard compile-time dependency on its
implementation.

### Architecture patterns used

- **MVI presentation layer**: each feature screen has a `State`, a sealed `Action`, an optional
  sealed `Event` (for one-off effects like navigation), a `ViewModel` exposing
  `StateFlow<UiState<State>>` and an `onAction(Action)` entry point, and one or more `UseCase`s.
  The same `ViewModel` instance drives both the Compose UI (Android) and the SwiftUI views (iOS,
  via SKIE-generated Swift bindings and Flow-to-`async`-sequence bridging).
- **Feature registry + event bus** (`core/registry`): features implement `FeatureApi` and
  register themselves with a `FeatureRegistry` at app startup (`androidApp`'s `Application`
  class / iOS's `IosFeatureBootstrap`), so other code can look a feature up by type, check its
  availability, or query it by route. Cross-feature navigation (e.g., Settings → Countries) goes
  through a `FeatureEventBus` instead of a direct dependency, so features stay decoupled.
  `FeatureLoader` additionally demonstrates dynamic, on-device feature-module loading on Android
  (`DexClassLoader`-based); iOS statically links all features instead, since dynamic code loading
  isn't available on that platform.
- **Dependency injection**: [Koin](https://insert-koin.io/), with per-layer modules
  (`core-network`, `core-domain`, feature modules, etc.) assembled by `initKoin()` in
  `core-di`, plus a Koin-scoped `BaseScopedViewModel` for feature-scoped dependencies.
- **Networking**: [Ktor](https://ktor.io/) client (`core-network`), with the country data
  loaded from a public JSON endpoint.
- **Persistence**: [Room](https://developer.android.com/kotlin/multiplatform/room) (KMP) for
  structured data (feature-availability records), and Jetpack DataStore for key-value
  preferences — including an encrypted preferences path backed by Tink/Android Keystore on
  Android and Keychain-backed AES-GCM on iOS.
- **Navigation**: type-safe [Compose Navigation](https://developer.android.com/guide/navigation)
  with a bottom `NavigationBar` on Android; native `NavigationStack` + `TabView` on iOS.

## Running it

- **Android**: open the project in Android Studio and run the `androidApp` configuration, or
  `./gradlew :androidApp:assembleDebug` from the command line.
- **iOS**: open [`/iosApp`](./iosApp) in Xcode and run it from there.
- **Web**: present in `settings.gradle.kts` but not currently functional — `shared` doesn't
  define the `App()` composable `webApp`'s entry point expects. Treat this target as inactive
  until that's wired up.

## Tests

`feature/feature-country/src/commonTest` exists as a source set but doesn't yet have test
coverage for the search/filter logic — a natural next addition given the debounce and
client-side filtering behavior in `CountriesViewModel`.
