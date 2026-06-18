---
name: block-figma-map
description: >-
  Map of the BLOCK Figma file (key R1bw3ysZmoZ83l0VCfUTCt) — which page holds what.
  Load this FIRST whenever working in BLOCK's Figma: creating or updating any screen, component,
  flow, or the design system (colors/type/tokens), or when a figma.com/design/R1bw3ysZmoZ... link
  appears. Tells you which page to read/edit so you don't rediscover the structure each time.
---

# BLOCK Figma file map

All BLOCK design work lives in one Figma file: **key `R1bw3ysZmoZ83l0VCfUTCt`**
("BLOCK — Design System / Auth Flows"). Before creating or updating anything, figure out which
**page** owns it from the table below, then inspect that page first. Don't guess node IDs — read
the page.

## Pages (use the id with get_metadata / get_screenshot / use_figma)

| Page                 | id      | What lives here — go here when the task is about…                                                                                                                                                  |
|----------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Foundations**      | `24:2`  | The design system: color tokens (palette + semantic, Dark/Light modes), type scale, fonts, spacing/radius dimensions, chart colors. Anything about **colors, fonts, theme, tokens**.               |
| **Components**       | `2:2`   | **Reusable components** — buttons, fields, icons (`icon/*`), rows, chips, nav, sheets, etc. Look here FIRST before building any new component; reuse/extend instead of recreating.                 |
| **Auth Flows**       | `2:3`   | **Authentication** screens & flows (login, registration, etc.). Anything about the auth flow.                                                                                                      |
| **Spending Tracker** | `121:7` | The assembled **Spending Tracker** app screens & prototype (home, transactions, insights, capture, limits, splits, refunds, owed, empty states). Anything about spending-tracker screens or flows. |
| **Prototype**        | `24:3`  | Prototype scaffolding.                                                                                                                                                                             |
| **Cover**            | `0:1`   | File cover.                                                                                                                                                                                        |

## How to work in this file

1. **Identify the page** from the table, then read it (`get_metadata` for structure,
   `get_screenshot`
   for visuals, `use_figma` for programmatic inspection).
2. **Reuse before creating.** New screen → reuse existing **Components** (`2:2`) and bind
   colors/type
   to **Foundations** (`24:2`) tokens. Never hardcode hex/sizes that a token already covers. New
   component → add it to the Components page following the existing pattern.
3. **Dark/Light parity** comes from the Foundations color collection (`VariableCollectionId:4:3`,
   Dark mode `4:1` / Light mode `23:0`). Bind fills to variables so modes can't drift.
4. **`use_figma` requires the `figma-use` skill first** (mandatory) — load it before any write.

## Gotchas

- `get_metadata` with **no nodeId** only lists some top-level pages (Cover/Components). To see ALL
  pages, run `figma.root.children` via `use_figma`, or pass the page id directly from the table.
- Dark/Light are **separate sibling frames** per screen (e.g. `· Dark` / `· Light`), not one frame
  with a mode toggle.

## Deeper references (this project's memory)

- **Foundations → code:** `memory/designkit-theme-foundation.md` — how Foundations tokens map into
  `:core:designkit` theme (M3 roles, fonts, Dimens/ChartColors).
- **Spending Tracker nav model:** `memory/spending-tracker-figma-navmodel.md` — chrome pattern,
  navigation/reaction model, key node IDs.
- **Design-system overview:** `memory/block-figma-design-system.md`.
