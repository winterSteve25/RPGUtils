package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class InteractBlockTrigger {
    
    private final BlockPos pos;
    private final ServerWorld world;
    
    public InteractBlockTrigger(BlockPos pos, ServerWorld world) {
        this.pos = pos;
        this.world = world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ServerWorld getWorld() {
        return world;
    }
}
