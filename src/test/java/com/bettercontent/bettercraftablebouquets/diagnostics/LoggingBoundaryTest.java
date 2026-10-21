package com.bettercontent.bettercraftablebouquets.diagnostics;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/** Supplementary wiring/privacy invariant, not the actual emitter or native behavior test. */
final class LoggingBoundaryTest {
    @Test void authoritativeFeatureBoundaryAndPrivacyRemainExplicit() throws Exception {
        String source = Files.readString(Path.of("src/main/java/com/bettercontent/bettercraftablebouquets/menu/BouquetGridMenu.java")) +
            Files.readString(Path.of("src/main/java/com/bettercontent/bettercraftablebouquets/diagnostics/FeatureObservation.java"));
        assertTrue(source.contains("FeatureObservation.menu"), "FeatureObservation.menu");
        assertTrue(source.contains("bouquet.rotate"), "bouquet.rotate");
        assertTrue(source.contains("bouquet.transfer"), "bouquet.transfer");
        assertTrue(source.contains("native_rotation"), "native_rotation");
        assertTrue(source.contains("native_transfer"), "native_transfer");
        assertFalse(source.contains("fields(\"nbt\""));
        assertFalse(source.contains("fields(\"payload\""));
        assertFalse(source.contains("fields(\"seed\""));
        assertTrue(source.contains("enabled("));
    }
}
