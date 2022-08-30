package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.BlockPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractBlockTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class InteractWithBlockObjective extends TriggeredObjective<InteractBlockTrigger> {
    
    private final BlockPredicate predicate;
    
    public InteractWithBlockObjective(BlockPredicate blockPredicate) {
        super(InteractBlockTrigger.class, trigger -> blockPredicate.matches(trigger.getWorld(), trigger.getPos()));
        predicate = blockPredicate;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", "interactBlock");
        jsonObject.add("predicate", predicate.serializeToJson());
        
        return jsonObject;
    }

    public static class Deserializer implements IDeserializer<InteractWithBlockObjective> {
        @Override
        public InteractWithBlockObjective fromJson(JsonObject jsonObject) {
            return new InteractWithBlockObjective(BlockPredicate.fromJson(jsonObject.get("predicate")));
        }
    }
}
