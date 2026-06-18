package com.basim.block.core.designkit.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * BLOCK raw color palette — mirrors the Figma foundation (`palette` + `chart` collections).
 * These are the raw ramps; semantic M3 roles are mapped from them in [DarkColorScheme] /
 * [LightColorScheme] (see Theme.kt). Prefer referencing `MaterialTheme.colorScheme.*` or the
 * extended token holders over using these constants directly.
 */

// Ink — warm neutral ramp
internal val Ink0 = Color(0xFFFBFAF7)
internal val Ink50 = Color(0xFFF4F2EC)
internal val Ink100 = Color(0xFFE9E6DD)
internal val Ink200 = Color(0xFFD7D3C8)
internal val Ink300 = Color(0xFFB9B4A8)
internal val Ink400 = Color(0xFF8C887D)
internal val Ink500 = Color(0xFF635F57)
internal val Ink600 = Color(0xFF403D38)
internal val Ink700 = Color(0xFF2A2824)
internal val Ink800 = Color(0xFF1A1916)
internal val Ink900 = Color(0xFF121110)
internal val Ink950 = Color(0xFF0C0B0A)
internal val PureWhite = Color(0xFFFFFFFF)

// Emerald (em) — brand secondary ramp
internal val Em50 = Color(0xFFE9F5EF)
internal val Em100 = Color(0xFFCBE9DA)
internal val Em200 = Color(0xFF9AD3B8)
internal val Em300 = Color(0xFF62B892)
internal val Em400 = Color(0xFF2F9A6E)
internal val Em500 = Color(0xFF1E7D58)
internal val Em600 = Color(0xFF166347)
internal val Em700 = Color(0xFF114E39)
internal val Em800 = Color(0xFF0D3B2C)
internal val Em900 = Color(0xFF0A2A20)

// Champagne (ch) — brand tertiary ramp
internal val Ch200 = Color(0xFFEBDDB9)
internal val Ch300 = Color(0xFFDCC78C)
internal val Ch400 = Color(0xFFC9AE63)
internal val Ch500 = Color(0xFFB8974A)
internal val Ch600 = Color(0xFF997B39)

// Error (red)
internal val Red100 = Color(0xFFF7E2E0)
internal val Red500 = Color(0xFFC8453C)
internal val Red500Dark = Color(0xFFE2675E)
internal val Red900 = Color(0xFF4A140F)

// Scrim — Figma scrim, ink/950 @ 60% (same in both modes)
internal val Scrim = Color(0x990C0B0A)

// Accents used by chart / limit tokens
internal val Amber500 = Color(0xFFC98A2E)
internal val LimitOver = Color(0xFFB5642E)

// Chart categorical palette
internal val Chart1 = Color(0xFF2F9A6E)
internal val Chart2 = Color(0xFF2F8A9A)
internal val Chart3 = Color(0xFFC9AE63)
internal val Chart4 = Color(0xFFC98A2E)
internal val Chart5 = Color(0xFF7D9A78)
internal val Chart6 = Color(0xFF8C887D)

// Heatmap ramp (emerald-based)
internal val Heat0 = Color(0xFF0A2A20)
internal val Heat1 = Color(0xFF114E39)
internal val Heat2 = Color(0xFF1E7D58)
internal val Heat3 = Color(0xFF2F9A6E)
internal val Heat4 = Color(0xFF62B892)
