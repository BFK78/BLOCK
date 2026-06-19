# Spending Tracker — Build Spec (V1)

> Agent-facing spec for the BLOCK Spending Tracker. Read this before building,
> styling, or reviewing any screen in this section. When a request maps to a
> feature below, reference its ID (e.g. "implement SP-C5"). When something here
> conflicts with a request, surface the conflict before building.

## 1. What we're building

The Spending Tracker is the flagship section and the **first bottom-nav tab**. It
answers exactly one question, honestly: **"where did my money go?"**

It is deliberately *not* the other two sections — keep these boundaries sharp:

| Section              | Question it answers               | Status           |
|----------------------|-----------------------------------|------------------|
| **Spending Tracker** | "Where did my money go?"          | This spec        |
| Wealth tracker       | "Am I getting richer?"            | Separate section |
| Financial Health     | "Is my spending pattern healthy?" | Separate section |

V1 is a **polished product, not an MVP**. Capture = SMS parsing + manual entry +
receipt scanning, all funnelling into one confirmation experience.

## 2. Invariants (do not violate)

These are product guarantees. If a feature or layout would break one, stop and flag it.

1. **Headline = consumption only.** The hero "spent" total sums money *consumed*. Money that left
   the account but is still the user's — `Owed to me`, or moved to savings/investment — is tracked
   but **excluded** from the headline.
2. **Outflow-only.** This section never surfaces income or inflows. Income is consent-gated and
   lives in Goals / Financial Health.
3. **Capture once, route everywhere.** One classification at capture drives this section's math and
   feeds signals to other sections. The user never logs the same thing twice.
4. **The spending limit is a mirror, not a gate.** It reflects reality (the spend already happened).
   It informs and paces; it never blocks or shames.
5. **Insights are observational and kind.** Never judgmental, never moralizing.

## 3. Scope

**In scope (V1):** spend capture (SMS, manual, scan), transaction classification,
confirmation/review experience, transaction history + detail, slicing & insights,
spending limits, owed-to-me tracking, refunds, splits, Light/Dark.

**Out of scope — lives elsewhere:**

| Concern                                                  | Owner                    |
|----------------------------------------------------------|--------------------------|
| Income / inflows                                         | Goals / Financial Health |
| Recurring-subscription detection                         | Financial Health         |
| Investment value / returns                               | Wealth tracker           |
| Full repayment-collection workflow                       | Separate section         |
| Wealth / Financial Health / Goals tabs, Profile/Settings | App shell                |

The avatar and the permission-denied "Open settings" path route to a
Profile/Settings placeholder **owned by the shell**, not this section.

## 4. Screen map (information architecture)

**Home / Overview** is the hub. From it:

- **Confirmation / Review** — persistent "N spends to review" launcher → Confirm Stack →
  all-caught-up state.
- **Add spend** — FAB → capture chooser → either:
    - *Scan a receipt*: Scan → Extracting → Pick Total → Confirm Receipt (+ OCR-failure,
      camera-permission-denied states)
    - *Add manually*: Manual Entry
- **Transactions** — "View all" → full history → Transaction Detail.
- **Insights** — "See all insights" → Insights → Insights Detail.
- **Spending limit** — Limit Setup.
- **Owed-to-me** — per-person and per-transaction views.
- **Refund flow** — Log refund → Apply → Result (incl. standalone-refund result).

**Empty states** exist for Home, Transactions, and Insights (first-run).
**Light and Dark** are full parallel sets via variable modes.

## 5. Feature catalog

### 5.1 Capture

- **SP-C1 · Unified confirmation card.** All sources (SMS, scan, manual) converge on one card. Two
  modes: *detected* (amount, merchant, best-guess category prefilled — "tap to confirm") and *manual
  prompt* (blank invitation to add).
- **SP-C2 · One-tap category confirmation.** Best-guess category pre-selected; 3–4 one-tap
  alternative chips; full list one tap deeper.
- **SP-C3 · Review stack (batching).** Pending spends as a light card stack — confirm each in a tap,
  with skip-for-now. Persistent Home launcher ("N spends to review") opens the stack and remains
  after skipping. All-caught-up state closes the loop.
- **SP-C4 · Manual entry.** Fast amount → category. Cash is a first-class payment method. Optional
  tags, note, receipt; editable/back-datable date. Type defaults to `Spent`.
- **SP-C5 · Receipt scanning.** Scan → Extracting → Pick Total (multi-amount bills) → Confirm
  Receipt (lands as a guess in the confirmation card, **never auto-saved**). Includes OCR-failure (
  retry + manual fallback) and camera-permission-denied (settings + manual fallback) states.
- **SP-C6 · Capture chooser.** Home FAB opens a chooser: Scan or Manual.

### 5.2 Transaction model & classification

- **SP-T1 · Transaction types.** Every outflow classified at capture: `Spent` (consumed — counts),
  `Owed to me` (expected back — excluded), `Self-transfer / Investment` (moved — excluded).
- **SP-T2 · "Owed to me."** Merges friend-loans, group bills, work-reimbursement. Has a source
  sub-tag (friend / group / work) and a settled / not-settled status.
- **SP-T3 · Split display.** When part is expected back, amount renders as a sum (e.g.
  `₹4,000 + ₹1,000`), expected-back portion visually distinct and tappable → detail. One consistent
  visual rule app-wide.
- **SP-T4 · Self-transfer dual-track.** Home hero stays consumption-only; a quiet secondary line
  shows "moved to savings/investments." The moved total never enters spend charts. (No investment
  value/returns here — that's Wealth.)
- **SP-T5 · Cross-category tags.** Tags ("Goa trip", "Wedding") cut across categories to answer "
  what did that whole thing cost."
- **SP-T6 · Per-transaction richness.** Category, type, tags, note, attachable receipt on every
  transaction.

### 5.3 Home / Overview

- **SP-H1 · Hero spend figure** for current period (consumption only) + period control.
- **SP-H2 · Dual-track secondary line** ("moved to savings/investments").
- **SP-H3 · Spending limit indicator** — reactive and pace-aware (see 5.6).
- **SP-H4 · Single AI insight card** — exactly one high-signal observation; the rest live in
  Insights.
- **SP-H5 · Recent transactions** — tappable rows → Transaction Detail, with "View all".
- **SP-H6 · Persistent chrome** — bottom nav (Spending Tracker active), FAB, avatar, review
  launcher.

### 5.4 Transactions list & detail

- **SP-L1 · History** — full list, grouped by date, newest first.
- **SP-L2 · Filter & sort** — by type, category, payment method, tag (apply / clear) + sort.
- **SP-L3 · Transaction Detail** — full info, inline editors for category/type/tags/note, date
  picker for back-dating.
- **SP-L4 · Actions menu** — Edit, Delete (with confirm), Duplicate.
- **SP-L5 · Receipt viewer** — full-screen image from thumbnail or link.

### 5.5 Splits, owed-to-me & refunds

- **SP-S1 · Split across categories** — divide one spend across multiple categories.
- **SP-S2 · Split with people** — add people, equal or custom shares; reconciles with split-display
  amount and Owed-to-me total.
- **SP-S3 · Owed-to-me list** — dedicated view: per-person rollup + per-transaction rows, source
  tabs (All / Friend / Group / Work), show-settled toggle, settle status.
- **SP-R1 · Refund netting** — on logging a refund, pick from a searchable, recent-first list of
  spends to net against. Supports partial refunds and a no-match fallback (standalone credit). Both
  matched and standalone paths end on a result screen showing the adjusted amount.

### 5.6 Spending limit

- **SP-M1 · Limit setup** — overall monthly limit + optional limits on top categories, with presets
  and amount entry.
- **SP-M2 · Reactive indicator** — comfortable / approaching / at-or-over states. Calm and
  factual ("₹8,200 of ₹8,000 — ₹200 over"); awareness, not alarm. Pace-aware ("60% used · 10 days
  left"). A mirror, not a gate — limit-setting and reflection here; behavioral coaching belongs in
  Financial Health.

### 5.7 Insights

- **SP-I1 · Period control** — week / month / longer; shared period selector (month / week / custom
  range) used across Home, Insights, and the list.
- **SP-I2 · Category breakdown** — visual spend-by-category.
- **SP-I3 · Spend trend** over time.
- **SP-I4 · Merchant view** — ranked spend by merchant ("₹14,200 at Swiggy this year").
- **SP-I5 · Payment-method view** — UPI / card / cash.
- **SP-I6 · Calendar heatmap** — daily spend intensity.
- **SP-I7 · AI insights feed** — anomaly detection, end-of-month natural-language summaries,
  merchant patterns. Observational, never moralizing.
- **SP-I8 · Insights Detail** — drill-down by category, merchant, or tag into the period's
  transactions.

### 5.8 System / cross-cutting

- **SP-X1 · Light & Dark** — full parity via shared variable modes.
- **SP-X2 · Material 3 Expressive** — physics-based motion, shape morphing, emphasized type scale,
  expressive shapes, M3 Expressive bottom nav.
- **SP-X3 · Empty states** — first-run states for Home, Transactions, and Insights.