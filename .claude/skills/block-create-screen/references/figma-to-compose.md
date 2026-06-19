# Figma → Compose extraction

How to read a BLOCK Figma screen and translate it faithfully into the project's theme and
components.
Read this before writing the composable in Phase 3.

## Table of contents

1. [Reading the frame](#1-reading-the-frame)
2. [Token → code mapping](#2-token--code-mapping)
3. [Existing :core:designkit components](#3-existing-coredesignkit-components)
4. [When designkit is missing a component](#4-when-designkit-is-missing-a-component)
5. [Assets (icons, images)](#5-assets-icons-images)
6. [M3 expressive](#6-m3-expressive)

---

## 1. Reading the frame

After `block-figma-map` tells you the page, inspect the screen with the Figma MCP:

- `get_metadata` (page id or frame node id) — the node tree, names, and ids. Use it to understand
  hierarchy (which Column/Row/Card wraps what) and to find reusable component instances by name.
- `get_screenshot` (frame node id) — the visual target. **Save it**; Phase 4 diffs against it.
- `get_variable_defs` (frame node id) — the exact design variables the frame binds (color/space/type
  tokens). This tells you *which* token to map to, so you don't eyeball a hex that's actually a
  named
  token.

Figma frames are usually auto-layout → translate directly to `Column`/`Row` with
`Arrangement.spacedBy(...)` for gaps and `padding(...)` for insets. A frame's "gap" is a spacing
token
(map to `LocalDimens`), not a magic number. Dark/Light are separate sibling frames — build the one
the
user asked for; the theme handles the other mode automatically once colors are bound to roles.

## 2. Token → code mapping

The designkit theme is mapped 1:1 from the Figma Foundations page. Always prefer the token over a
literal. (Deeper detail: `memory/designkit-theme-foundation.md`.)

| Figma foundation                                              | Code                                                 |
|---------------------------------------------------------------|------------------------------------------------------|
| Color tokens (semantic roles: surface, primary, onSurface, …) | `MaterialTheme.colorScheme.<role>`                   |
| Type scale (display/headline/title/body/label …)              | `MaterialTheme.typography.<style>`                   |
| Spacing scale (gap/padding: 2,4,8,12,16,20,24,32,40,48,56,64) | `LocalDimens.current.spacing<N>`                     |
| Corner radius (xs/sm/md/lg/xl/2xl/full)                       | `LocalDimens.current.radius<Size>`                   |
| Touch target / control height                                 | `LocalDimens.current.touchTarget` / `.controlHeight` |
| Chart colors                                                  | `ChartColors` holder (see theme)                     |
| Gradients                                                     | `GradientColors` / `Gradient.kt`                     |

`Dimens` is at `core/designkit/.../designsystem/theme/Dimens.kt`; read it for the exact field names
if
unsure. If Figma uses a value no token covers, use the literal **and note it** so the user can
decide
whether to add a token.

## 3. Existing :core:designkit components

Reuse these before writing anything new (package
`com.basim.block.core.designkit.designsystem.*`):

- `component.BlockButton`, `component.BlockIconButton` — buttons (filled / icon+text, e.g. "Continue
  with Google").
- `component.BlockTextField` — text input with label/placeholder.
- `component.BlockBackground` — the app background wrapper (use it in every `@Preview`).
- `icon.BlockIcons` — the icon set (`BlockIcons.ArrowBack`, `BlockIcons.GoogleIcon`, …).
- `theme.BlockTheme` — the theme wrapper; `theme.LocalDimens`, `theme.ChartColors`,
  `theme.Gradient`,
  `theme.Tint`.

Browse `core/designkit/src/main/kotlin/com/basim/block/core/designkit/designsystem/component/` for
the
current full list before assuming something is missing — the set grows over time.

## 4. When designkit is missing a component

If the Figma **Components** page shows a reusable component that designkit doesn't have yet:

- It belongs in `:core:designkit` (cross-feature) — **not** inlined into the screen. Build it there,
  mirroring the existing component style (preview with `@PreviewLightDark`, theme-bound colors).
- If it's only repeated within this one feature, the feature's `common/components/` is the home (
  e.g.
  the existing `authentication/common/components/Dividers.kt`).
- Adding a new shared component widens the design system — **confirm with the user** before doing
  it,
  and keep it token-bound so Dark/Light can't drift.

## 5. Assets (icons, images)

- Vector icons: prefer an existing `BlockIcons` entry. If the frame uses a new icon, export it from
  Figma (`download_assets`/`upload_assets` via the Figma MCP) as a vector drawable into
  `core/designkit/src/main/res/drawable/` (designkit owns shared icons) and add it to `BlockIcons`.
- Raster images specific to one screen go in that feature module's `res/drawable*`.
- Keep resource names within the module's auto-prefix.

## 6. M3 expressive

Expressive components are encouraged here. They live in `androidx.compose.material3` (the Compose
BOM
in this project ships them) and are mostly experimental, so opt in explicitly at the function:

```kotlin
@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
```

The app's `BlockTheme` currently wraps the standard `MaterialTheme` (not `MaterialExpressiveTheme`),
so use expressive **components** within the existing theme — do not swap the theme wrapper. If a
design
clearly needs `MaterialExpressiveTheme`-level defaults (motion/shape system-wide), raise it with the
user rather than migrating the theme inside a screen task.
