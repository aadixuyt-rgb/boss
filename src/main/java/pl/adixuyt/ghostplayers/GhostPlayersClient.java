package pl.adixuyt.ghostplayers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class GhostPlayersClient implements ClientModInitializer {
    /** true = other players are see-through and their hitboxes are skipped. Default: off. */
    public static boolean enabled = false;

    private static KeyBinding toggleKey;
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        GhostConfig.load();

        // These are the only two GhostPlayers keybinds exposed in Minecraft Controls.
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostplayers.toggle", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.ghostplayers"));
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostplayers.gui", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K, "key.categories.ghostplayers"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
            }
            while (guiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new GhostScreen());
            }
        });
    }
}
