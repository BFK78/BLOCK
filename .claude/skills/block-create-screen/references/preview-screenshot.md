# Verify pixel-perfect: render the preview & compare

Phase 4 of the workflow. The screen is only "done" when its rendered `@Preview` matches the Figma
screenshot. This renders the Compose preview directly (no emulator) and diffs it.

## Prerequisite: Android Studio must be running with the project open

`render-compose-preview` talks to a live Android Studio instance — it renders the actual preview the
IDE would. If it's not open, ask the user to open the project in Android Studio (or run it via the
`run` skill), then continue. Load the **`android-cli`** skill if the `android` CLI isn't installed.

Check what's available:

```bash
android studio check            # lists running Studio instances / open projects
```

## Render the preview

```bash
android studio render-compose-preview \
  "features/<name>/src/main/kotlin/com/basim/block/features/<name>/presentation/<screen>/<Screen>Screen.kt" \
  "<Screen>ScreenPreview" \
  --output-image-file=/tmp/<screen>_preview.png
```

- `<path>` — the file holding the `@Preview` (relative to repo root or absolute).
- `<composable>` — the **preview function** name (the `@Preview private fun …Preview`), not the
  screen
  function.
- `--output-image-file` — where to write the PNG.
- `--project` / `--pid` — pass these if `android studio check` shows multiple projects/instances.
- `--print-semantics` — add when you need the semantics tree to debug why something renders wrong.

## Compare and iterate

1. Open both images side by side: the Figma screenshot saved in Phase 1 and
   `/tmp/<screen>_preview.png`
   (use the Read tool — it renders images visually).
2. Walk top-to-bottom and check, in priority order: overall layout/structure → spacing & alignment →
   typography (size/weight/line height) → colors → corner radius/elevation → iconography.
3. For each mismatch, fix the composable — usually a token swap (`spacing16` vs `spacing24`, a
   `typography` style, a `colorScheme` role) rather than a literal. Re-render and re-compare.
4. Repeat until the two are visually indistinguishable.

## When you can't reach pixel-perfect

Stop and tell the user precisely what's blocking, instead of shipping an approximation. Common
blockers:

- **A value has no matching token** — the design uses a spacing/color/radius the foundation doesn't
  define. Report the exact Figma value; ask whether to add a token or use a one-off literal.
- **A component can't be expressed** with current designkit/M3 — describe the gap.
- **An asset can't be extracted** from Figma — name the node.
- **Android Studio isn't running / render fails** — surface the command output.

Pixel-perfect is the bar, but a clearly-explained blocker is the right outcome when the bar
genuinely
can't be met with what's available.
