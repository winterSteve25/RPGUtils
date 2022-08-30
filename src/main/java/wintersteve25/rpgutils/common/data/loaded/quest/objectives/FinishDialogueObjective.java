package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.FinishDialogueTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class FinishDialogueObjective extends TriggeredObjective<FinishDialogueTrigger> {
    
    private final ResourceLocation dialogue;
    
    public FinishDialogueObjective(ResourceLocation dialogue) {
        super(FinishDialogueTrigger.class, trigger -> dialogue.equals(trigger.getDialogue()));
        this.dialogue = dialogue;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", "finishDialogue");
        jsonObject.addProperty("dialogue", dialogue.toString());
        
        return jsonObject;
    }

    public static class Deserializer implements IDeserializer<FinishDialogueObjective> {
        @Override
        public FinishDialogueObjective fromJson(JsonObject jsonObject) {
            return new FinishDialogueObjective(new ResourceLocation(jsonObject.get("dialogue").getAsString()));
        }
    }
}
