package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.adixuyt.ghostplayers.GhostFlag;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

/** Keeps remote players' armor and head equipment visible so it can be rendered translucent. */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At("RETURN"))
    private void ghostplayers$makeTranslucent(AbstractClientPlayerEntity player, PlayerEntityRenderState state,
                                              float tickDelta, CallbackInfo ci) {
        boolean ghost = GhostPlayersClient.enabled && player != MinecraftClient.getInstance().player;
        ((GhostFlag) state).ghostplayers$setGhost(ghost);
        if (ghost) {
            state.invisible = true;
            state.invisibleToPlayer = false;
            state.displayName = null;
        }
    }
}
