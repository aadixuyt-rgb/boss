package pl.adixuyt.ghostplayers.mixin;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pl.adixuyt.ghostplayers.GhostFlag;

/** Stores the ghost marker on every living-entity render state, not just players. */
@Mixin(LivingEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin implements GhostFlag {
    @Unique private boolean ghostplayers$ghost;

    @Override public boolean ghostplayers$isGhost() { return ghostplayers$ghost; }
    @Override public void ghostplayers$setGhost(boolean ghost) { this.ghostplayers$ghost = ghost; }
}
