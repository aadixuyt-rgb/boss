package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/** Uses a blended entity layer for living-entity models, including armor models. */
@Mixin(Model.class)
public abstract class ModelMixin {
    @Inject(method = "getLayer", at = @At("RETURN"), cancellable = true)
    private void ghostplayers$translucentEntityLayer(Identifier texture, CallbackInfoReturnable<RenderLayer> cir) {
        if (GhostPlayersClient.enabled && (Object) this instanceof EntityModel<?>) {
            cir.setReturnValue(RenderLayer.getEntityTranslucent(texture));
        }
    }
}
