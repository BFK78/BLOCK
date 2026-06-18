# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Working approach

Before non-trivial build/change work, pressure-test intent per the clarify-intent skill: challenge
costly-ambiguous, irreversible, or architectural decisions before executing; for small clear tasks,
just build.

## Project

**Block** — an Android app (`applicationId = com.basim.block`) built with Jetpack Compose, organized
as a multi-module Gradle project with custom convention plugins. Min SDK 24, target/compile SDK 34.

## Commands

All commands run via the Gradle wrapper from the repo root. Note the directory path contains a
space ("MOBILE APPS"), so quote paths when scripting.

```bash
./gradlew assembleDebug            # build debug APK
./gradlew build                    # full build (all modules)
./gradlew test                     # JVM unit tests (all modules)
./gradlew :features:authentication:testDebugUnitTest   # unit tests for one module
./gradlew connectedAndroidTest     # instrumented tests (needs device/emulator)
./gradlew lint                     # Android lint (configured via block.android.lint)
./gradlew :app:installDebug        # install debug build on connected device
```

To run a single unit test class/method:

```bash
./gradlew :core:designkit:testDebugUnitTest --tests "com.basim.block.core.designkit.ExampleUnitTest"
```

Module graph tooling (defined in root `build.gradle.kts`):

```bash
./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=all_modules
./gradlew generateModulesGraphStatistics -Pmodules.graph.output.gv=all_modules
./gradlew printModulePaths          # list all module paths
```

## Architecture

### Module structure

Three module groups, defined in `settings.gradle.kts`:

- **`:app`** — the application entry point (`MainActivity`). Currently still on the Android Studio
  template (package `com.basim.convention`, applies raw `androidApplication`/
  `jetbrainsKotlinAndroid` plugins rather than the convention plugins — unlike the rest of the
  project).
- **`:features:*`** — feature modules (e.g. `:features:authentication` with `login` and
  `registration` screens). Features depend on core modules but **never on each other**.
- **`:core:*`** — shared modules (e.g. `:core:designkit`, the design system: theming, Compose
  components, icons). Core modules **never depend on features**.

### Enforced dependency rules (`moduleGraphAssert` in root `build.gradle.kts`)

These are asserted on every build (`assertOnAnyBuild = true`), so a violating dependency fails the
build:

- Allowed: `:features:* -> :core:*`, `:app -> :features:*`, `:app -> :core:*`
- Restricted: `:core:* -X> :features:*`, `:features:* -X> :features:*`
- Max module-graph height: 3

### Convention plugins (`build-logic/`)

Shared build logic lives in the `build-logic` included build. Module `build.gradle.kts` files apply
these instead of repeating configuration. Plugin IDs map to classes in
`build-logic/convention/src/main/kotlin/` (registered in `build-logic/convention/build.gradle.kts`):

- `block.android.application` / `block.android.application.compose`
- `block.android.library` → `AndroidLibraryConventionPlugin` (base for all library modules: Kotlin
  config, lint, SDK, resource prefixing)
- `block.android.library.compose` → enables Compose via `configureAndroidCompose`
- `block.android.feature` → `AndroidFeatureConventionPlugin` — applies `block.android.library` +
  `block.android.hilt` and adds Compose navigation/lifecycle/Hilt-navigation deps. **Use this for
  new feature modules.**
- `block.android.hilt` → applies KSP + Hilt and wires `hilt-android`/`hilt-compiler` (KSP, not kapt)
- `block.android.lint`

A typical feature module's `build.gradle.kts` is just:

```kotlin
plugins {
    alias(libs.plugins.block.android.feature)
    alias(libs.plugins.block.android.library.compose)
}
android { namespace = "com.basim.block.features.<name>" }
dependencies { implementation(project(":core:designkit")) }
```

### Resource prefixing

`AndroidLibraryConventionPlugin` auto-derives a `resourcePrefix` from the module path. Resources in
a module **must** be prefixed accordingly — e.g. strings in `:features:authentication` use the
`features_authentication_` prefix, resources in `:core:designkit` use `core_designkit_`.

### Dependencies

- All versions and library/plugin aliases are centralized in `gradle/libs.versions.toml` (version
  catalog). Add new dependencies there and reference via `libs.*`.
- In `:core:designkit`, public Compose dependencies are exposed with `api(...)` so consuming modules
  inherit them; module-internal deps use `implementation(...)`.
- DI is **Hilt** (with **KSP**, not kapt). Compose compiler extension `1.5.13`; convention plugins
  use JVM target 17 while app/library modules compile to JVM target 1.8.
