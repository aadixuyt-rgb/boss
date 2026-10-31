package pl.adixuyt.ghostplayers;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Opened with K: on/off button + slider 0-100 % (how much other players are hidden). */
public final class GhostScreen extends Screen {
    public GhostScreen() {
        super(Text.literal("GhostPlayers"));
    }

    private static Text toggleText() {
        return Text.literal("Ukrywanie graczy: " + (GhostPlayersClient.enabled ? "WŁĄCZONE" : "WYŁĄCZONE"));
    }

    @Override
    protected void init() {
        int w = 200, x = width / 2 - w / 2, y = height / 2 - 30;

        addDrawableChild(ButtonWidget.builder(toggleText(), b -> {
            GhostPlayersClient.enabled = !GhostPlayersClient.enabled;
            b.setMessage(toggleText());
        }).dimensions(x, y, w, 20).build());

        addDrawableChild(new SliderWidget(x, y + 28, w, 20,
                Text.literal("Ukrycie: " + GhostConfig.hidePercent + "%"), GhostConfig.hidePercent / 100.0) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Ukrycie: " + GhostConfig.hidePercent + "%"));
            }

            @Override
            protected void applyValue() {
                GhostConfig.hidePercent = GhostConfig.clamp((int) Math.round(value * 100.0));
            }
        });

        addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> close())
                .dimensions(x, y + 56, w, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 52, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("0% = widoczni, 100% = całkiem niewidoczni"), width / 2, height / 2 + 54, 0xA0A0A0);
    }

    @Override
    public void removed() {
        GhostConfig.save();
    }
}
