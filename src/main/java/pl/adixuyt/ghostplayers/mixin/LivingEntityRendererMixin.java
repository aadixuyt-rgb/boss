package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.adixuyt.ghostplayers.GhostConfig;
import pl.adixuyt.ghostplayers.GhostFlag;
import pl.adixuyt.ghostplayers.GhostPlayersClient;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    private static final String RENDER = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;"
            + "Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V";

    /** Mark all remote living entities, including mobs, as ghosts while the feature is enabled. */
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At("RETURN"))
    private void ghostplayers$markLivingEntity(LivingEntity entity, LivingEntityRenderState state,
                                               float tickDelta, CallbackInfo ci) {
        boolean ghost = GhostPlayersClient.enabled && entity != MinecraftClient.getInstance().player;
        ((GhostFlag) state).ghostplayers$setGhost(ghost);
        if (ghost) {
            state.invisible = true;
            state.invisibleToPlayer = false;
        }
    }

    /** 100 % = fully hidden, using the same opacity for the model and its features. */
    @Inject(method = RENDER, at = @At("HEAD"), cancellable = true)
    private void ghostplayers$hideCompletely(LivingEntityRenderState state, MatrixStack matrices,
                                             VertexConsumerProvider consumers, int light, CallbackInfo ci) {
        if (GhostPlayersClient.enabled && GhostConfig.hidePercent >= 100
                && state instanceof GhostFlag flag && flag.ghostplayers$isGhost()) {
            ci.cancel();
        }
    }

    /** Vanilla's fixed ghost tint (0x26FFFFFF, ~15 % alpha) -> alpha chosen in the GUI. */
    @ModifyConstant(method = RENDER, constant = @Constant(intValue = 654311423))
    private int ghostplayers$ghostColor(int original) {
        return GhostPlayersClient.enabled ? GhostConfig.color() : original;
    }
}
