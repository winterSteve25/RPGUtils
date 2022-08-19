package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.BlockPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractBlockTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class InteractWithBlockObjective extends TriggeredObjective<InteractBlockTrigger> {
    public InteractWithBlockObjective(BlockPredicate blockPredicate) {
        super(InteractBlockTrigger.class, trigger -> blockPredicate.matches(trigger.getWorld(), trigger.getPos()));
    }
    
    public static class Deserializer implements IDeserializer<InteractWithBlockObjective> {
        @Override
        public InteractWithBlockObjective fromJson(JsonObject jsonObject) {
            return new InteractWithBlockObjective(BlockPredicate.fromJson(jsonObject.get("predicate")));
        }
    }
}
