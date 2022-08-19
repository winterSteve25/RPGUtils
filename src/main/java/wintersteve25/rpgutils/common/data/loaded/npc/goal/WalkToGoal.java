package wintersteve25.rpgutils.common.data.loaded.npc.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.registry.ModMemoryModuleTypes;

import java.util.EnumSet;

public class WalkToGoal extends Goal {

    private final NPCEntity entity;

    public WalkToGoal(NPCEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return entity.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        return entity.getNavigation().isInProgress();
    }

    @Override
    public void start() {
//        RPGUtils.LOGGER.info("Started WalkToGoal");
        Path path = entity.getNavigation().createPath(entity.getBrain().getMemory(ModMemoryModuleTypes.MOVEMENT_TARGET.get()).orElseThrow(RuntimeException::new), 1);
        entity.getNavigation().moveTo(path, 1.0D);
    }
}
