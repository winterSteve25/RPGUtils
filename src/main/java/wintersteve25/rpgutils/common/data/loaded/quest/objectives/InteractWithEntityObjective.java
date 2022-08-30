package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.EntityPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractEntityTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class InteractWithEntityObjective extends TriggeredObjective<InteractEntityTrigger> {
    
    private final EntityPredicate predicate;
    
    public InteractWithEntityObjective(EntityPredicate entityPredicate) {
        super(InteractEntityTrigger.class, trigger -> entityPredicate.matches(trigger.getPlayer(), trigger.getInteracted()));
        predicate = entityPredicate;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        
        object.addProperty("type", "interactEntity");
        object.add("predicate", predicate.serializeToJson());
        
        return object;
    }

    public static class Deserializer implements IDeserializer<InteractWithEntityObjective> {
        @Override
        public InteractWithEntityObjective fromJson(JsonObject jsonObject) {
            return new InteractWithEntityObjective(EntityPredicate.fromJson(jsonObject.get("predicate")));
        }
    }
}