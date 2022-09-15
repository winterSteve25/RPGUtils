package wintersteve25.rpgutils.common.data.loaded.npc.goal;

import net.minecraft.entity.ai.goal.Goal;
import wintersteve25.rpgutils.common.entities.NPCEntity;

import java.util.function.Function;

public class ModGoals {

    public static final GoalConstructor WALK_TO = WalkToGoal::new;
    public static final GoalConstructor ATTACK = NPCAttackGoal::new;

    public interface GoalConstructor extends Function<NPCEntity, Goal> {}
}
