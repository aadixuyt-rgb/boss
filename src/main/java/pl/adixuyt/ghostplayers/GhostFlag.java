package pl.adixuyt.ghostplayers;

/** Duck interface added to PlayerEntityRenderState: marks a player drawn as a ghost. */
public interface GhostFlag {
    boolean ghostplayers$isGhost();
    void ghostplayers$setGhost(boolean ghost);
}
