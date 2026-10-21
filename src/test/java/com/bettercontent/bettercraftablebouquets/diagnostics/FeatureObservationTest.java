package com.bettercontent.bettercraftablebouquets.diagnostics;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;
/** Actual production projection test; native gameplay gates own whole-feature state/RNG equivalence. */
final class FeatureObservationTest {
    public static void main(String[] args) { new FeatureObservationTest().actualEmitterStagesPrivacyAndObservationalPurity(); }
    @Test void actualEmitterStagesPrivacyAndObservationalPurity() {
        var records = new ArrayList<String>(); boolean[] enabled = {false};
        var log = new PlaytestLog("better_craftable_bouquets", new PlaytestLog.Sink() {
            public boolean enabled(String category, String level) { return enabled[0]; }
            public void write(String category, String level, String text) { records.add(text); }
        }, () -> 0L, new Properties());
        var contextReads = new AtomicInteger();
        Supplier<PlaytestLog.Context> ctx = () -> { contextReads.incrementAndGet(); return PlaytestLog.Context.server("minecraft:overworld", 10); };
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000042");
        // Observational purity at this scalar projection seam only, not a synthetic native replay.
        String rotationRejection = "empty_slot";
        long[] state = {42, 3}; long[] before = state.clone();
        var nativeRng = new Random(41); var baselineRng = new Random(41);
        FeatureObservation.menu(log, ctx, "bouquet.rotate", "committed", "native_rotation", 2, 4, 1);
        FeatureObservation.menu(log, ctx, "bouquet.rotate", "rejected", rotationRejection, 2, 4, 0);
        FeatureObservation.menu(log, ctx, "bouquet.transfer", "committed", "native_transfer", 2, 4, 3);
        FeatureObservation.menu(log, ctx, "bouquet.transfer", "rejected", "grid_full_or_no_progress", 2, 4, 0);
        assertTrue(records.isEmpty()); assertEquals(0, contextReads.get());
        assertArrayEquals(before, state); assertEquals(baselineRng.nextLong(), nativeRng.nextLong());
        enabled[0] = true;
        FeatureObservation.menu(log, ctx, "bouquet.rotate", "committed", "native_rotation", 2, 4, 1);
        FeatureObservation.menu(log, ctx, "bouquet.rotate", "rejected", rotationRejection, 2, 4, 0);
        FeatureObservation.menu(log, ctx, "bouquet.transfer", "committed", "native_transfer", 2, 4, 3);
        FeatureObservation.menu(log, ctx, "bouquet.transfer", "rejected", "grid_full_or_no_progress", 2, 4, 0);
        assertEquals(4, records.size());
        assertTrue(records.get(0).contains("result=committed reason=native_rotation"), records.toString());
        assertTrue(records.get(1).contains("result=rejected reason=empty_slot"), records.toString());
        assertTrue(records.get(2).contains("result=committed reason=native_transfer"), records.toString());
        assertTrue(records.get(3).contains("result=rejected reason=grid_full_or_no_progress"), records.toString());
        assertTrue(records.stream().noneMatch(s -> s.contains("arrangement=")), records.toString());
        assertTrue(records.stream().noneMatch(s -> s.contains("inventory=")), records.toString());
        assertTrue(records.stream().noneMatch(s -> s.contains("nbt=")), records.toString());
        assertArrayEquals(before, state); assertEquals(baselineRng.nextLong(), nativeRng.nextLong());
    }
}
