package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;

public class InteractEntityTrigger {
    private final Entity interacted;
    private final ServerWorld world;
    
    public InteractEntityTrigger(Entity interacted, ServerWorld world) {
        this.interacted = interacted;
        this.world = world;
    }

    public Entity getInteracted() {
        return interacted;
    }

    public ServerWorld getWorld() {
        return world;
    }
}
