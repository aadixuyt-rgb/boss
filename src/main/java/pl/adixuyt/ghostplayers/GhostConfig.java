package pl.adixuyt.ghostplayers;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Tiny persisted settings: how much other players are hidden (0-100 %). */
public final class GhostConfig {
    private GhostConfig() {}

    /** 0 = fully visible, 100 = fully invisible. */
    public static int hidePercent = 85;

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("ghostplayers.properties");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) return;
        try (Reader r = Files.newBufferedReader(f)) {
            Properties p = new Properties();
            p.load(r);
            hidePercent = clamp(Integer.parseInt(p.getProperty("hidePercent", "85").trim()));
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hidePercent", Integer.toString(hidePercent));
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "GhostPlayers");
        } catch (IOException ignored) {
        }
    }

    public static int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    /** ARGB colour for the translucent player model (white tinted, alpha = 100 - hide%). */
    public static int color() {
        int alpha = Math.round(255f * (100 - hidePercent) / 100f);
        return (alpha << 24) | 0x00FFFFFF;
    }
}
