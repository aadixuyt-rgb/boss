package pl.adixuyt.ghostplayers;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Persisted transparency settings for player skins and worn equipment. */
public final class GhostConfig {
    private GhostConfig() {}

    /** 0 = fully visible, 100 = fully invisible. */
    public static int hidePercent = 85;
    /** Independent transparency for armor, worn heads and other equipment. */
    public static int equipmentHidePercent = 85;

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
            equipmentHidePercent = clamp(Integer.parseInt(
                    p.getProperty("equipmentHidePercent", "85").trim()));
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hidePercent", Integer.toString(hidePercent));
        p.setProperty("equipmentHidePercent", Integer.toString(equipmentHidePercent));
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "GhostPlayers");
        } catch (IOException ignored) {
        }
    }

    public static int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    /** ARGB colour for the translucent entity model. */
    public static int color() {
        int alpha = Math.round(255f * (100 - hidePercent) / 100f);
        return (alpha << 24) | 0x00FFFFFF;
    }

    /** Applies the independent equipment transparency to an ARGB model colour. */
    public static int equipmentColor(int originalColor) {
        int originalAlpha = (originalColor >>> 24) & 0xFF;
        int alpha = Math.round(originalAlpha * (100 - equipmentHidePercent) / 100.0f);
        return (originalColor & 0x00FFFFFF) | (alpha << 24);
    }
}
