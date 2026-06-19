---
name: block-architecture
description: >-
  The single source of truth for the Block Android app's architecture (com.basim.block) — the whole
  app, not just features. Use this skill for ANY structural change anywhere in the repo: in a feature
  module, in a :core:* module, or in :app. Consult it whenever you add or change a feature, screen,
  ViewModel, reducer, use-case, repository, DAO, a shared/core module (design kit, database, util),
  the app entry point, DI wiring, navigation host, or the module graph — even when the user doesn't
  say "architecture" or name a layer. Triggers: "add a <x> feature", "new screen", "create a
  ViewModel/state holder", "wire up the login flow", "store this with Room", "add a repository/
  use-case", "add a core/shared module", "put this in designkit", "change the app entry point / DI /
  navigation", "MVI", "handle this UI event". It encodes the rules: three module groups
  (:app, :core:*, :features:*) with an enforced dependency graph; one Gradle module per feature;
  layered data/domain/presentation packages; Clean Architecture with a pure-Kotlin domain; MVI
  presentation with a pure-Kotlin reducer; Kotlin Flows/StateFlow + coroutines; one-shot UI events
  via Channel; Hilt + KSP DI; Room for persistence. Consult it before scaffolding so the result
  matches the project's conventions and passes the enforced module-graph rules.
---

# Block architecture

The architecture reference for the **entire** Block app — a modern, multi-module Jetpack Compose
project (`applicationId = com.basim.block`, min SDK 24). It governs all three module groups: the
`:app` entry point, the shared `:core:*` modules, and the `:features:*` modules. Any structural
change in the repo — a new feature, a new core module, app-level DI/navigation, the module graph —
should follow this skill so the result is consistent and passes the build's enforced rules.

> Read `CLAUDE.md` at the repo root for module/convention-plugin basics. This skill is the layer on
> top: how the app is actually structured and how to extend any part of it.

## The three module groups

```
:app            the application: entry point (MainActivity), DI host, navigation host, splash.
                Depends on features + core. Uses RAW plugins today (not the convention plugins).
:core:*         shared building blocks reused by features: design system, database, utilities.
                Library modules. Never depend on a feature.
:features:*     one module per feature (e.g. :features:authentication). Self-contained Clean-
                Architecture slice. Depends on core; never on another feature.
```

- **Working in `:app` or a `:core:*` module?** → read `references/module-conventions.md` first.
- **Building/changing a feature, screen, or data/domain/presentation code?** → start below and use
  the feature references.

## The golden rules (enforced — a violation fails the build)

`moduleGraphAssert` runs on every build (`assertOnAnyBuild = true`). These are hard constraints:

- **Allowed:** `:features:* -> :core:*`, `:app -> :features:*`, `:app -> :core:*`
- **Forbidden:** `:core:* -X> :features:*`, `:features:* -X> :features:*`
- Max module-graph height: 3.

So: a feature may depend on core modules, never on another feature. Core never depends on a feature.
Shared code goes **down** into a `:core:*` module, never sideways between features.

## One module per feature

Each feature is a **single** Android library module under `:features:<name>` (e.g.
`:features:authentication`). All of that feature's logic lives inside it, split into three
packages —
not three modules:

```
features/<name>/
├── build.gradle.kts
└── src/main/
    ├── AndroidManifest.xml            # empty <manifest>, no package attr
    ├── kotlin/com/basim/block/features/<name>/      # NOTE: kotlin/, not java/
    │   ├── data/
    │   │   ├── local/                 # Room entities + DAOs
    │   │   ├── remote/                # (only if the feature has an API — optional)
    │   │   ├── mapper/                # entity/DTO <-> domain model
    │   │   ├── repository/            # XxxRepositoryImpl : domain repository interface
    │   │   └── di/                    # Hilt @Module for this feature's data layer
    │   ├── domain/                    # PURE KOTLIN — no Android/Compose/Hilt/Room imports
    │   │   ├── model/                 # plain data classes
    │   │   ├── repository/            # repository INTERFACES
    │   │   └── usecase/               # one class per use-case
    │   └── presentation/
    │       ├── <screenA>/             # one package per screen
    │       │   ├── <ScreenA>Contract.kt   # UiState + UiEvent + UiEffect
    │       │   ├── <ScreenA>Reducer.kt     # PURE KOTLIN
    │       │   ├── <ScreenA>ViewModel.kt   # @HiltViewModel
    │       │   └── <ScreenA>Screen.kt      # @Composable
    │       ├── <screenB>/
    │       └── navigation/            # this feature's nav graph/routes (optional)
    └── common/components/             # composables shared across this feature's screens
```

Module wiring — a feature's `build.gradle.kts` is just:

```kotlin
plugins {
    alias(libs.plugins.block.android.feature)          // applies library + hilt + KSP, adds
    alias(libs.plugins.block.android.library.compose)  // ViewModel/lifecycle-compose + hilt-navigation
}
android { namespace = "com.basim.block.features.<name>" }
dependencies { implementation(project(":core:designkit")) }
```

`block.android.feature` already pulls in Hilt, KSP, `lifecycle-runtime-compose`,
`lifecycle-viewmodel-compose`, and `hilt-navigation-compose`. Don't re-add them. Resources are
auto-prefixed `features_<name>_` (e.g. string ids start with `features_authentication_`).

## The three layers in one breath

- **domain** — the feature's contract and business rules, in **pure Kotlin**. Models, repository
  *interfaces*, and use-cases. No Android, Compose, Hilt, or Room. This is what makes the rules
  testable with plain JUnit and keeps the feature's core stable.
- **data** — implements the domain repository interfaces. Room DAOs/entities (returning `Flow`),
  mappers to domain models, and a Hilt `@Module` that binds impls and provides DAOs.
- **presentation** — MVI screens. UI renders state and emits events; it holds no business logic.

## The MVI loop

```
Screen --(UiEvent)--> ViewModel --(call)--> UseCase --> Repository
   ^                      |
   |                      | result/partial change
   |                      v
   |                   Reducer (pure) --> new UiState
   |                      |
   +--(UiState via StateFlow)----+
   +--(UiEffect via Channel: one-shot nav/snackbar)--+
```

- **UiState** — one immutable `data class`, exposed as `StateFlow<UiState>`.
- **UiEvent** — `sealed interface` of everything the user can do on the screen.
- **UiEffect** — `sealed interface` of one-shot side effects (navigate, show snackbar). Delivered
  through a **`Channel` + `receiveAsFlow()`**, *not* a `StateFlow`/`SharedFlow`, so each effect
  fires
  exactly once and isn't replayed on recomposition or config change.
- **Reducer** — a pure function `(state, change) -> state`. No coroutines, no Android. It's the one
  place new state is computed, which is why it stays pure and unit-testable.
- **ViewModel** — `@HiltViewModel`, owns the `MutableStateFlow` and the effect `Channel`, runs
  use-cases in `viewModelScope`, and routes results through the reducer.

## Threading & reactivity

Kotlin coroutines + Flow throughout. Room DAOs and repositories expose `Flow` for streams; use-cases
`suspend` or return `Flow`; ViewModels collect in `viewModelScope` and publish `StateFlow`. Use a
`Channel` only for one-shot effects.

## Where to go next

Pick the reference file for the task at hand:

- **Working in `:app` or a `:core:*` module; adding a shared/core module; app DI/navigation; how
  module groups, convention plugins, namespaces & source dirs work** →
  `references/module-conventions.md`
- **Building/laying out a feature's layers, repositories, mappers, Hilt modules, Room** →
  `references/layer-responsibilities.md`
- **Writing the screen: Contract / Reducer / ViewModel / Screen with full code** →
  `references/mvi-contract.md`
- **Spinning up a brand-new feature module step by step (incl. project gaps to fix first)** →
  `references/new-feature-checklist.md`

## Before you scaffold: known repo gaps

The infrastructure for MVI exists but some pieces aren't wired yet. Don't assume they're present —
check and add as needed (details + exact catalog entries in `references/new-feature-checklist.md`):

- **App isn't Hilt-enabled.** `:app` doesn't apply Hilt; there's no `@HiltAndroidApp` Application
  and
  `MainActivity` isn't `@AndroidEntryPoint`. Feature `@HiltViewModel`s won't resolve until that's
  fixed. `:app` also doesn't depend on `:features:authentication` yet.
- **No coroutines library alias** in `gradle/libs.versions.toml` (only a compiler opt-in). Add
  `kotlinx-coroutines-*` when you write coroutine code directly.
- **No Room library aliases** (`androidx-room-runtime/-ktx/-compiler`) and no Room convention
  plugin —
  only the version and gradle-plugin exist. Add aliases before using Room.
- **No general `navigation-compose` alias** — only `hilt-navigation-compose`.
