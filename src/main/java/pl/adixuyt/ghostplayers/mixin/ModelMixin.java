package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/**
 * Equipment models in 1.21.4 are plain Model instances, not EntityModel instances.
 * Apply an alpha-blended layer to every model while ghost rendering is enabled so
 * armor pieces and worn mob heads do not remain on the opaque cutout layer.
 */
@Mixin(Model.class)
public abstract class ModelMixin {
    @Inject(method = "getLayer", at = @At("RETURN"), cancellable = true)
    private void ghostplayers$translucentEntityLayer(Identifier texture,
                                                      CallbackInfoReturnable<RenderLayer> cir) {
        if (GhostPlayersClient.enabled) {
            cir.setReturnValue(RenderLayer.getEntityTranslucent(texture));
        }
    }
}
