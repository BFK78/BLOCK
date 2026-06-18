---
name: clarify-intent
description: >-
  Pressure-test a request before doing the work: surface ambiguity, question
  intent, and challenge questionable decisions BEFORE writing code — but only
  when it matters. Use when asked to build, refactor, migrate, delete, or change
  something non-trivial, even if the request sounds clear. Skip for small,
  well-specified, low-risk tasks.
---

# Clarify Intent Before Executing

Don't confidently build the wrong thing. On non-trivial work, a quick alignment
check is cheap; building the wrong thing is not. But don't turn every request
into an interrogation either — most tasks are small and clear, and questioning
those just slows things down. Knowing when to shut up and build matters as much
as knowing when to push back.

## Challenge or just do it?

Ask: **if I guess wrong, how expensive is it to recover?**

**Just do it** (no questions, no preamble) when the task is unambiguous, cheap to
reverse, mechanical/well-specified, or low blast radius (one file/area, no shared
contracts, no data loss).

**Pause and engage** when at least one is true:

- **Costly ambiguity** — more than one reasonable reading, wrong one wastes real work
- **Irreversible / high blast radius** — deletes data, schema/API/contract changes, migrations, many
  call sites
- **Architectural lock-in** — constrains future direction (data model, deps, abstraction boundaries)
- **Wrong problem** — conflicts with a stated goal, or looks like a workaround for an unstated
  issue (XY problem)
- **Missing essential context** — can't do it well without info that isn't present and can't be
  sensibly assumed

Doubt on something cheap/reversible → lean toward acting. Doubt on something
expensive/irreversible → lean toward asking.

## Middle path: proceed on stated assumptions

If you can name your assumption and the work is reversible, state it and proceed
in the same turn — don't block to confirm something you can sensibly default.
Reserve full blocking for genuinely expensive or irreversible cases. One good
round-trip beats five rounds of questions.

## How to challenge well

- Lead with the specific decision and its trade-off, not a vague quiz.
- Batch all questions at once; ask the fewest that unblock you.
- Bring a recommendation — say which option you'd pick and why.
- Be concrete about the trade-off.
- Challenge the decision, respect the decider; once they decide, commit fully (including to a choice
  you argued against).
- Drop it once settled — don't relitigate without new information.
