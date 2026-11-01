package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.adixuyt.ghostplayers.GhostConfig;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/**
 * Armor and wearable head items use a cutout render layer by default, which ignores
 * per-vertex alpha. Switch it to Minecraft's armor-translucent layer while ghost mode
 * is active, and apply the same configured alpha as the entity skin.
 */
@Mixin(EquipmentRenderer.class)
public abstract class EquipmentRendererMixin {
    private static final String RENDER_WITH_TEXTURE =
            "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;"
            + "Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;"
            + "Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V";

    @Redirect(
            method = RENDER_WITH_TEXTURE,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer ghostplayers$useTranslucentArmorLayer(Identifier texture) {
        if (GhostPlayersClient.enabled) {
            return RenderLayer.createArmorTranslucent(texture);
        }
        return RenderLayer.getArmorCutoutNoCull(texture);
    }

    @ModifyArg(
            method = RENDER_WITH_TEXTURE,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
            ),
            index = 4
    )
    private int ghostplayers$armorOpacity(int originalColor) {
        if (!GhostPlayersClient.enabled) return originalColor;
        int originalAlpha = (originalColor >>> 24) & 0xFF;
        int opacity = 100 - GhostConfig.hidePercent;
        int alpha = Math.round(originalAlpha * (opacity / 100.0f));
        return (originalColor & 0x00FFFFFF) | (alpha << 24);
    }
}
