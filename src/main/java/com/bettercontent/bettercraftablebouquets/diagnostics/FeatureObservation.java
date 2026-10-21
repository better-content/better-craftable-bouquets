package com.bettercontent.bettercraftablebouquets.diagnostics;
import java.util.function.Supplier;
/** Pure owner-specific projection of already computed native outcomes; no gameplay writes or RNG. */
public final class FeatureObservation {
    private FeatureObservation() {}
    public static void menu(PlaytestLog log, Supplier<PlaytestLog.Context> context, String event, String result, String reason, int menu, int slot, int amount) {
        if (!log.enabled("menu")) return;
        log.debug("menu", event, context.get(), result, reason, () -> PlaytestLog.fields("menu", menu, "slot", slot, "amount", amount));
    }
    public static void decision(PlaytestLog log, String category, String event, String result, String reason) {
        if (!log.enabled(category)) return;
        log.event(category, event, "server", result, reason, () -> "");
    }
}
