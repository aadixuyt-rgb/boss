package pl.adixuyt.ghostplayers;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Opened with K: toggle plus independent skin and equipment transparency sliders. */
public final class GhostScreen extends Screen {
    public GhostScreen() {
        super(Text.literal("GhostPlayers"));
    }

    private static Text toggleText() {
        return Text.literal("Ukrywanie graczy: " + (GhostPlayersClient.enabled ? "WŁĄCZONE" : "WYŁĄCZONE"));
    }

    @Override
    protected void init() {
        int w = 220, x = width / 2 - w / 2, y = height / 2 - 58;

        addDrawableChild(ButtonWidget.builder(toggleText(), b -> {
            GhostPlayersClient.enabled = !GhostPlayersClient.enabled;
            b.setMessage(toggleText());
        }).dimensions(x, y, w, 20).build());

        addDrawableChild(new SliderWidget(x, y + 27, w, 20,
                Text.literal("Przezroczystość skina: " + GhostConfig.hidePercent + "%"),
                GhostConfig.hidePercent / 100.0) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Przezroczystość skina: " + GhostConfig.hidePercent + "%"));
            }

            @Override
            protected void applyValue() {
                GhostConfig.hidePercent = GhostConfig.clamp((int) Math.round(value * 100.0));
            }
        });

        addDrawableChild(new SliderWidget(x, y + 54, w, 20,
                Text.literal("Przezroczystość setu i główek: " + GhostConfig.equipmentHidePercent + "%"),
                GhostConfig.equipmentHidePercent / 100.0) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Przezroczystość setu i główek: "
                        + GhostConfig.equipmentHidePercent + "%"));
            }

            @Override
            protected void applyValue() {
                GhostConfig.equipmentHidePercent =
                        GhostConfig.clamp((int) Math.round(value * 100.0));
            }
        });

        addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> close())
                .dimensions(x, y + 81, w, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 80, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("0% = widoczne, 100% = całkiem niewidoczne"),
                width / 2, height / 2 + 53, 0xA0A0A0);
    }

    @Override
    public void removed() {
        GhostConfig.save();
    }
}
