package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class InteractEntityTrigger {
    private final Entity interacted;
    private final ServerPlayerEntity player;
    
    public InteractEntityTrigger(Entity interacted, ServerPlayerEntity player) {
        this.interacted = interacted;
        this.player = player;
    }

    public Entity getInteracted() {
        return interacted;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }
}
