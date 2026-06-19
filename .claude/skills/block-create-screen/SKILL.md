---
name: block-create-screen
description: >-
  Build a single pixel-perfect Compose screen for the Block Android app (com.basim.block) from its
  Figma design. Use this whenever the user asks to create, build, implement, or "make" a screen, page,
  or view from Figma — e.g. "create the spending tracker home screen", "build the transactions screen",
  "implement the insights page", "add the empty-state screen", even when they don't say "Figma" or
  "pixel perfect". Figma is the source of truth: this skill locates the screen in the BLOCK Figma file
  first, then writes ONLY the screen composable + a @Preview (no ViewModel, Contract, Reducer, nav, or
  DI), reusing core/designkit + Figma-Components, binding every text/color/space/type to strings.xml and
  the M3 theme, and verifying against the Figma screenshot until it matches. Do NOT use this for
  ViewModels/business logic/navigation wiring (that's block-architecture), for editing the design system
  itself, or for pushing code back into Figma.
---

# Block — create a pixel-perfect screen

Turn one Figma screen into one Compose screen that matches it pixel-for-pixel. **Scope is
deliberately
narrow: the screen composable and its `@Preview` only.** No ViewModel, Contract, Reducer,
navigation,
or Hilt — those come later via `block-architecture`. Keeping the surface to "just the UI" is what
lets
us iterate fast on visual fidelity without dragging in state plumbing.

This skill leans on three sibling skills instead of duplicating them — load them as you hit each
phase:

- **`block-figma-map`** — which Figma page owns the screen (Figma file key
  `R1bw3ysZmoZ83l0VCfUTCt`).
- **`block-architecture`** — where the file goes in the module/package structure.
- **`android-cli`** — the `android` CLI used to render the preview for the pixel comparison.

## Non-negotiables (these are the whole point of the skill)

Break any of these and the screen is wrong even if it compiles:

1. **Figma is the source of truth.** Never invent layout, spacing, or color from imagination. Read
   the
   actual frame before writing a line of Compose.
2. **Every user-visible string lives in `strings.xml`**, referenced with `stringResource(...)`. No
   hardcoded text in the composable — not even `"Email"` or `"Cancel"`. Strings are prefixed
   `features_<name>_` (lint enforces the prefix). This is the rule most easily broken; hold it
   firmly.
3. **Bind to the M3 theme, never hardcode design values.** Colors come from
   `MaterialTheme.colorScheme.*`, type from `MaterialTheme.typography.*`, spacing/radius from
   `LocalDimens.current.*` (the `Dimens` token holder). Only fall back to a literal `dp`/`Color`
   when
   Figma uses a value no token covers — and call that out.
4. **Reuse before you build.** A component on the Figma **Components** page already exists (or
   should)
   in `:core:designkit`. Use it. Don't recreate a button/field/row that designkit already ships.
5. **Jetpack Compose + Material 3 only.** No XML layouts, no deprecated APIs. M3 **expressive**
   components are encouraged; experimental APIs are fine (opt in explicitly).
6. **Leave a Figma backlink** at the top of the screen composable so the next agent can re-open the
   exact frame (format below).
7. **Don't bloat one file — distribute meaningfully.** "One screen" is a scope of work, not a
   mandate to cram everything into one `.kt`. When the screen file grows past a comfortable read
   (rough rule: more than ~200 lines, or once it holds several distinct concerns), split the
   private helpers into sibling files in the **same screen package** — e.g. `<Screen>Screen.kt`
   (the screen composable + its `@Preview`), `<Screen>Page.kt` / section composables + their data
   models, `<Screen>Illustration.kt` for bespoke canvas/illustration drawing. Split by concern, not
   by line count alone; keep each file cohesive. Symbols shared across the split files become
   `internal` (still module-scoped); leaf helpers stay `private`. The public screen composable's
   signature never changes. This is still **one screen** — the split is about readability, not new
   scope, and these files stay UI-only (no ViewModel/Contract/Reducer/nav/DI). Reusable widgets
   still go to `:core:designkit` or the feature's `common/components/` per rule 4 — sibling files
   are for screen-private pieces only.

## Workflow

### Phase 1 — Locate the screen in Figma (source of truth)

1. Load **`block-figma-map`**. Pick the page that owns the screen (e.g. Spending Tracker `121:7`,
   Auth
   Flows `2:3`). Don't guess node IDs — read the page.
2. Use the Figma MCP to inspect the actual frame: `get_metadata` (structure/node IDs),
   `get_screenshot` (the visual target — **save this image**, you'll compare against it), and
   `get_variable_defs` (the exact tokens the design binds to). Dark/Light are separate sibling
   frames;
   confirm which one you're building (default to the one the user names, else Light).
3. Note the screen's frame node id and build the backlink URL (see the comment format below).

Full extraction details — token mapping, pulling assets, handling components — are in
`references/figma-to-compose.md`. Read it before writing the composable.

### Phase 2 — Decide where the file goes (block-architecture)

Load **`block-architecture`** and follow it for placement. In short:

- The screen belongs to a **feature module**: `:features:<name>` (e.g. Spending Tracker →
  `:features:spendingtracker`). Path:
  `features/<name>/src/main/kotlin/com/basim/block/features/<name>/presentation/<screen>/<Screen>Screen.kt`.
- **If the feature module doesn't exist yet, scaffold the minimal module** — `build.gradle.kts`
  (the two `block.android.feature` + `block.android.library.compose` plugins), an empty
  `AndroidManifest.xml`, `res/values/strings.xml`, and the `include(":features:<name>")` line in
  `settings.gradle.kts`. **Only the screen goes inside** — no `data/`, `domain/`, ViewModel, or DI.
  (See `block-architecture` → `references/new-feature-checklist.md` for the exact module files.)
- **Component placement:** if the thing you need is a reusable component (appears on the Figma
  **Components** page, or any feature would want it) → it belongs in `:core:designkit`. If it's only
  repeated within this one feature → the feature's `common/components/`. Never copy a component into
  a
  screen file.

### Phase 3 — Build the screen

Write `<Screen>Screen.kt` as a **stateless composable** plus a `@Preview`. Pattern:

```kotlin
package com.basim.block.features.<name > . presentation .<screen>

// ...imports

/**
 * <ScreenName> — pixel-built from Figma.
 * Figma: https://www.figma.com/design/R1bw3ysZmoZ83l0VCfUTCt/?node-id=<frame-node-id>
 * Page: <Page name> (<page id>)  ·  Mode: <Light|Dark>
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class) // only if you use an expressive/experimental API
@Composable
fun <Screen> Screen(
    modifier: Modifier = Modifier,
    // stateless hooks for later wiring — plain lambdas/values, NO ViewModel
    onBack: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens.spacing16),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
    ) {
        Text(
            text = stringResource(R.string.features_<name> _ < screen > _title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // ...reuse BlockButton / BlockTextField / BlockIconButton / etc. from :core:designkit
    }
}

@Preview
@Composable
private fun <Screen> ScreenPreview() {
    BlockTheme {
        BlockBackground {
            <Screen > Screen()
        }
    }
}
```

Rules while building (the *why* is in the Non-negotiables above):

- Pull every string into `features/<name>/src/main/res/values/strings.xml` with the
  `features_<name>_` prefix; reference via `stringResource`.
- Reuse `:core:designkit` components first (`BlockButton`, `BlockTextField`, `BlockIconButton`,
  `BlockBackground`, `BlockIcons`, …). If Figma's Components page shows a component designkit lacks,
  add it to designkit (not the screen) — confirm with the user before introducing a new shared
  component.
- Bind colors/type/spacing to the theme + `LocalDimens`. The token→code mapping cheatsheet and the
  list of existing designkit components are in `references/figma-to-compose.md`, with deeper detail
  in
  `memory/designkit-theme-foundation.md`.
- The `@Preview` MUST wrap the screen in `BlockTheme { BlockBackground { … } }` so it renders with
  the
  real design system — this is also what Phase 4 screenshots.

### Phase 4 — Verify pixel-perfect against Figma

Render the `@Preview` and diff it against the Figma screenshot from Phase 1. This is mandatory — a
screen isn't done until it matches. The render command (via a running Android Studio instance) and
the
compare-and-iterate loop are in `references/preview-screenshot.md`. In brief:

```bash
android studio render-compose-preview \
  "features/<name>/src/main/kotlin/.../<screen>/<Screen>Screen.kt" \
  "<Screen>ScreenPreview" \
  --output-image-file=/tmp/<screen>_preview.png
```

Compare `/tmp/<screen>_preview.png` to the Figma screenshot. Adjust spacing/type/color/layout and
re-render until they match. **If a gap can't be closed** (missing token, a component designkit can't
express, a Figma asset you can't extract, Android Studio not running), stop and surface it to the
user
with specifics rather than shipping an approximation.

## Done checklist

- [ ] Built from the actual Figma frame (backlink comment present, correct mode).
- [ ] Only the screen UI added — no ViewModel/Contract/Reducer/nav/DI. If large, split into cohesive
  sibling files in the screen package (screen + `@Preview`, sections/data, illustrations) rather
  than
  one bloated file.
- [ ] All strings in `strings.xml`, `features_<name>_`-prefixed, via `stringResource`.
- [ ] Colors/type/spacing bound to `MaterialTheme` + `LocalDimens`; literals only where no token
  fits
  (and flagged).
- [ ] Reused designkit components; any new reusable component went into `:core:designkit`, not the
  screen.
- [ ] Compose + M3 only; experimental opt-ins explicit; no XML, no deprecated APIs.
- [ ] Rendered preview matches the Figma screenshot (or blockers raised to the user).
