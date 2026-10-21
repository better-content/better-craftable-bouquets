# Bounded playtest observations

## Authority and feature scope

This repository owns the observations at `BouquetGridMenu`. Authoritative source remains the
behavior owner; the local full gate tests native behavior. Logging source/privacy contracts
are not pack runtime, performance or visual acceptance.

Only server-authoritative native rotation results and native quick-move outcomes are logged. Menu/slot, native UUID causal subject and scalar count/quarter-turn request correlate operations; no flower arrangement or inventory/NBT dump is retained. Empty/invalid/full/no-progress rejects are DEBUG, not errors. The native menu/container/packet/storage boundaries and both personal-inventory paths remain unchanged; paired native visual acceptance is independent and pending exact candidate review.

## Controls, bounds and lifecycle

The existing loader SLF4J backend owns `bc.playtest.better_craftable_bouquets.*`; no runtime provider
is bundled. Startup reports real mod version, effective loaded instrumentation and own-artifact
source metadata when available, otherwise UNKNOWN. Metadata is bounded and read only from this
mod's exact Forge ModFile at initialization. Normal standalone logging stays INFO; an explicitly
selected DEBUG profile enables causal records. One process-local instance shares 120 detail
records/minute, burst20, bounded256 aggregation keys and 2048-character single-line records.
Native END-server-tick boundaries coalesce monotonic30-second summaries; server stop clears
keys without replenishing the bucket or rearming TRACE. Actual failures preserve existing
normal severity/stack traces independently of detail suppression.

Focused TRACE remains default off and requires `bc.playtest.trace.better_craftable_bouquets.dimension`
and exactly one `.subject` UUID or `.pos=x,y,z`; the once-per-process window begins on the first
matching detail and expires at60seconds/1000records, also constrained by the common bucket.
These feature boundaries currently emit causal DEBUG, not per-tick/per-frame TRACE. The helper
supports focused details but a focus property does not invent an uninstrumented native observer.
Hot diagnostics guard before native context/field creation; no extra inventory/chunk scan,
RNG draw, entity construction, packet or persistent ledger is added.

## Native source fixture boundary

The source full gate uses a retained fresh `run-gametest/attempts/<UUID>` directory from Forge's
actual RunConfig, leaving shared client `run`, saves and old native evidence untouched. It
creates absent properties with native defaults only, guards preservation/ERROR-FATAL negatives,
and requires exactly7 authored native cases plus strict error-free diagnostics. This fixture
and new projection still need a fresh full gate. It never starts either native visual review;
paired personal-inventory/render acceptance remains separate and exact-artifact-bound.

## Verification boundary

The unchanged shared helper contract covers off/lazy behavior, escaping/truncation,
budget/cardinality/overflow/fakeclock/reset/focus and own-metadata validation. The owner-specific
source contract asserts actual event seams/privacy markers, not native success. Existing native
feature tests and the owning documented full gate remain required before publication. Exact
candidate gameplay, paired native UI review where relevant and performance acceptance are
separate; do not infer them from a causal log or a source-only pass.
