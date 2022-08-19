package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.EntityPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractEntityTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class InteractWithEntityObjective extends TriggeredObjective<InteractEntityTrigger> {
    public InteractWithEntityObjective(EntityPredicate entityPredicate) {
        super(InteractEntityTrigger.class, trigger -> entityPredicate.matches(trigger.getWorld(), trigger.getInteracted().position(), trigger.getInteracted()));
    }

    public static class Deserializer implements IDeserializer<InteractWithEntityObjective> {
        @Override
        public InteractWithEntityObjective fromJson(JsonObject jsonObject) {
            return new InteractWithEntityObjective(EntityPredicate.fromJson(jsonObject.get("predicate")));
        }
    }
}