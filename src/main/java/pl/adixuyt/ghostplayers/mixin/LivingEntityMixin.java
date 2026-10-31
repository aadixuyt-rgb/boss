package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/** Other players can't be targeted (crosshair / attack / interact) while the mod is on. Mobs are unaffected. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "canHit", at = @At("HEAD"), cancellable = true)
    private void ghostplayers$skipPlayerHitbox(CallbackInfoReturnable<Boolean> cir) {
        if (GhostPlayersClient.enabled && (Object) this instanceof AbstractClientPlayerEntity) {
            cir.setReturnValue(false);
        }
    }
}
