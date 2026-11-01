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

/** Makes worn armor and head equipment use the same opacity as the entity skin. */
@Mixin(EquipmentRenderer.class)
public abstract class EquipmentRendererMixin {
    private static final String RENDER_7 =
            "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;"
            + "Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;"
            + "Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/VertexConsumerProvider;I)V";

    private static final String RENDER_8 =
            "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;"
            + "Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;"
            + "Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V";

    /*
     * Both overloads exist in Minecraft 1.21.4. Different equipment features use
     * different overloads, so apply the translucent layer to either one.
     */
    @Redirect(
            method = {RENDER_7, RENDER_8},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            ),
            require = 0
    )
    private RenderLayer ghostplayers$useTranslucentEquipmentLayer(Identifier texture) {
        if (GhostPlayersClient.enabled) return RenderLayer.createArmorTranslucent(texture);
        return RenderLayer.getArmorCutoutNoCull(texture);
    }

    @ModifyArg(
            method = {RENDER_7, RENDER_8},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
            ),
            index = 4,
            require = 0
    )
    private int ghostplayers$equipmentOpacity(int originalColor) {
        if (!GhostPlayersClient.enabled) return originalColor;
        int originalAlpha = (originalColor >>> 24) & 0xFF;
        int alpha = Math.round(originalAlpha * ((100 - GhostConfig.hidePercent) / 100.0f));
        return (originalColor & 0x00FFFFFF) | (alpha << 24);
    }
}
