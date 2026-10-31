package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pl.adixuyt.ghostplayers.GhostFlag;

@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin implements GhostFlag {
    @Unique private boolean ghostplayers$ghost;

    @Override public boolean ghostplayers$isGhost() { return ghostplayers$ghost; }
    @Override public void ghostplayers$setGhost(boolean ghost) { this.ghostplayers$ghost = ghost; }
}
