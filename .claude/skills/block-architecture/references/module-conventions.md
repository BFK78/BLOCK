# Module conventions (`:app`, `:core:*`, `:features:*`)

How the whole module system is wired: the three groups, the convention plugins, namespaces, source
dirs, and how to add or change app- and core-level code. For a feature's *internal* structure
(data/domain/presentation, MVI) use the other references; this file is about the modules themselves.

## Table of contents

1. [The enforced module graph](#1-the-enforced-module-graph)
2. [Convention plugins (build-logic)](#2-convention-plugins-build-logic)
3. [`:app` module](#3-app-module)
4. [`:core:*` modules](#4-core-modules)
5. [`:features:*` modules](#5-features-modules)
6. [Adding a new module (settings + folders)](#6-adding-a-new-module)
7. [Shared conventions across all modules](#7-shared-conventions)

---

## 1. The enforced module graph

`moduleGraphAssert` (root `build.gradle.kts`, `assertOnAnyBuild = true`) fails the build on any
violation:

- **Allowed:** `:features:* -> :core:*`, `:app -> :features:*`, `:app -> :core:*`
- **Forbidden:** `:core:* -X> :features:*`, `:features:* -X> :features:*`
- Max graph height: 3.

Mental model: dependencies point **downward** only. `:app` sits on top and can see everything;
`:features:*` sit in the middle and see only `:core:*`; `:core:*` sit at the bottom and see no
feature. When two features need the same thing, push it **down** into a `:core:*` module — never let
one feature reach into another.

---

## 2. Convention plugins (build-logic)

Shared Gradle logic lives in the `build-logic` included build. Modules apply these plugin IDs
instead
of repeating config. The IDs map to classes in
`build-logic/convention/src/main/kotlin/` (registered in `build-logic/convention/build.gradle.kts`):

| Plugin alias                                       | ID                                    | Use it for                                                                                                  |
|----------------------------------------------------|---------------------------------------|-------------------------------------------------------------------------------------------------------------|
| `libs.plugins.block.android.feature`               | `block.android.feature`               | **every feature module** — applies library + hilt + KSP, adds ViewModel/lifecycle-compose + hilt-navigation |
| `libs.plugins.block.android.library`               | `block.android.library`               | base for any `:core:*` library module (Kotlin, lint, SDK, resource prefix)                                  |
| `libs.plugins.block.android.library.compose`       | `block.android.library.compose`       | add Compose to a library (`configureAndroidCompose`)                                                        |
| `libs.plugins.block.android.hilt`                  | `block.android.hilt`                  | apply Hilt + KSP to a module that isn't a feature                                                           |
| `libs.plugins.block.android.lint`                  | `block.android.lint`                  | lint config (auto-applied by the library plugin)                                                            |
| `libs.plugins.block.android.application[.compose]` | `block.android.application[.compose]` | exists for the app, but `:app` currently uses raw plugins instead (see below)                               |

Key facts the plugins bake in:

- `AndroidFeatureConventionPlugin` = `block.android.library` + `block.android.hilt` + ViewModel/
  lifecycle-compose + hilt-navigation-compose. So feature modules get Hilt/KSP/ViewModel for free —
  **don't re-declare them**.
- `AndroidHiltConventionPlugin` applies `com.google.devtools.ksp` + the Hilt Gradle plugin and wires
  `hilt-android` / `hilt-compiler` via **KSP** (never kapt).
- `AndroidLibraryConventionPlugin` auto-derives `resourcePrefix` from the module path and adds the
  Kotlin/lint/SDK setup.
- **There is no pure-JVM / `java-library` convention plugin.** A Kotlin-only module would apply
  `org.jetbrains.kotlin.jvm` directly (or you'd add a new convention plugin). In this project the
  pure-Kotlin domain lives *inside* the feature's Android module as a package, so you don't normally
  need a JVM module — but know the option exists if a shared pure-Kotlin `:core` module is wanted.

---

## 3. `:app` module

The application entry point: `MainActivity`, the splash, and (once wired) the DI host and navigation
host. It depends on features and core and composes them into the running app.

Current state and conventions:

- **Uses raw plugins, not the convention plugins** (unlike the rest of the project):
  ```kotlin
  plugins {
      alias(libs.plugins.androidApplication)
      alias(libs.plugins.jetbrainsKotlinAndroid)
      alias(libs.plugins.kotlinCompose)
  }
  ```
  namespace/`applicationId` `com.basim.block`. Source dir is `src/main/java/` here (features/core
  use
  `kotlin/`).
- `MainActivity` is a plain `ComponentActivity` that installs the splash screen and
  `setContent { BlockTheme { ... } }`.
- **Hilt is NOT applied at the app level yet** — there is no `@HiltAndroidApp` Application and the
  activity is not `@AndroidEntryPoint`. Feature `@HiltViewModel`s will not resolve until this is
  done.
  To enable DI app-wide:
    1. Apply Hilt + KSP to `:app` (add `block.android.hilt`, or the raw `hilt` + `ksp` plugins to
       match
       the app's raw-plugin style).
    2. Add an `Application` annotated `@HiltAndroidApp` and register it in the manifest.
    3. Annotate `MainActivity` with `@AndroidEntryPoint`.
- `:app` does **not** depend on `:features:authentication` yet. Add
  `implementation(project(":features:<name>"))` for each feature whose screens the app hosts.
- **Navigation host** belongs here: the app owns the top-level `NavHost`/graph and stitches feature
  entry composables (e.g. `LoginRoute`) together. Each feature exposes its route(s); the app wires
  them and passes navigation callbacks down. Only `hilt-navigation-compose` exists in the catalog
  today — add an `androidx-navigation-compose` alias before adopting standard Compose Navigation.

When changing the app entry point, DI host, or navigation, keep the app thin: it **composes**
features, it doesn't contain feature business logic.

---

## 4. `:core:*` modules

Shared building blocks every feature can reuse. They are library modules and must **never** depend
on
a feature. Put something here the moment a second feature would need it.

Conventions:

- Apply `block.android.library` (+ `block.android.library.compose` if it has Compose, +
  `block.android.hilt` if it provides injectable bindings).
- Namespace `com.basim.block.core.<name>` (note: `:core:designkit`'s namespace is historically
  `...core.designsystem` while its source package is `...core.designkit` — match the existing module
  you're editing rather than assuming).
- Source dir `src/main/kotlin/`. Resources auto-prefixed `core_<name>_`.
- **`api(...)` vs `implementation(...)`:** expose a dependency with `api(...)` only when consumers
  are
  meant to use its types directly (designkit re-exposes Compose foundation/material3/runtime with
  `api` so feature modules inherit them). Keep module-internal deps as `implementation(...)`.

Existing / planned cores:

- **`:core:designkit`** (exists) — the design system: `BlockTheme`, Compose components, icons,
  colors/typography/dimens. UI building blocks reused across features live here, not in a feature's
  `common/`.
- **`:core:database`** (recommended, not created) — the shared Room `@Database` and its Hilt
  provider.
  Features contribute their own `@Entity`/`@Dao` and depend on this module. See
  `layer-responsibilities.md` → "Room".
- **`:core:common` / `:core:util`** (create on demand) — dispatcher providers, `Result` wrappers,
  extension functions, anything cross-cutting.

Before creating a new core module, prefer extending an existing one if it fits; a proliferation of
tiny cores raises graph height (cap is 3).

---

## 5. `:features:*` modules

One self-contained Clean-Architecture slice per feature. Covered in depth by the feature
references —
in short: apply `block.android.feature` + `block.android.library.compose`, namespace
`com.basim.block.features.<name>`, source under `src/main/kotlin/`, internal
`data`/`domain`/`presentation` packages, depend only on `:core:*`. See `new-feature-checklist.md`.

---

## 6. Adding a new module

`settings.gradle.kts` uses plain `include()`; the parent containers `:features` and `:core` are
already declared. A colon path maps directly to a folder path.

- **New feature:** `include(":features:<name>")` → folder `features/<name>/`.
- **New core module:** `include(":core:<name>")` → folder `core/<name>/`.

Then create `<group>/<name>/build.gradle.kts`, `src/main/AndroidManifest.xml` (empty `<manifest>`),
`src/main/kotlin/...`, and `src/main/res/` as needed. Build the module to let `moduleGraphAssert`
validate the edges:

```bash
./gradlew :<group>:<name>:assembleDebug
```

---

## 7. Shared conventions

Across every module:

- **Source dir:** `src/main/kotlin/` for features and core; `:app` uses `src/main/java/`.
- **Resource prefix** is auto-enforced from the module path (`features_<name>_`, `core_<name>_`) — a
  mis-prefixed resource fails lint.
- **Version catalog:** all versions/aliases live in `gradle/libs.versions.toml`; add new deps there
  and reference via `libs.*`. Catalog gaps to fix before use (details in
  `new-feature-checklist.md`):
  no `kotlinx-coroutines-*` alias, no `androidx-room-runtime/-ktx/-compiler` aliases, no general
  `navigation-compose` alias.
- **DI is Hilt with KSP** (never kapt) everywhere injection is used.
- **Don't redeclare** what a convention plugin already provides (e.g. Hilt/ViewModel deps in a
  feature
  module).
