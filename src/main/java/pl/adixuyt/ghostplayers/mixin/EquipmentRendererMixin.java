package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pl.adixuyt.ghostplayers.GhostConfig;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/** Applies the GUI opacity to armor and wearable head models without changing their tint. */
@Mixin(EquipmentRenderer.class)
public abstract class EquipmentRendererMixin {
    @ModifyArg(
            method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
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
