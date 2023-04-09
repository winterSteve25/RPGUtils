package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.FinishDialogueTrigger;

import java.util.function.Consumer;

public class FinishDialogueObjective extends TriggeredObjective<FinishDialogueTrigger> {

    private final String dialogue;

    public FinishDialogueObjective(String dialogue) {
        super(FinishDialogueTrigger.class, trigger -> dialogue.equals(trigger.getDialogue()));
        this.dialogue = dialogue;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", "finishDialogue");
        jsonObject.addProperty("dialogue", dialogue);

        return jsonObject;
    }
    
    @Override
    public ITextComponent objectiveTitle() {
        return new StringTextComponent("Finish dialogue: " + dialogue);
    }

    @Override
    public void openEditObjectiveMenu(Consumer<IObjective> newObjective, Runnable onComplete) {
        
    }


    public static class Type implements IObjectiveType<FinishDialogueObjective> {
        @Override
        public FinishDialogueObjective fromJson(JsonObject jsonObject) {
            return new FinishDialogueObjective(jsonObject.get("dialogue").getAsString());
        }

        @Override
        public void openConfigScreen(Consumer<FinishDialogueObjective> onSubmit, Runnable onCancel) {
            
        }

        @Override
        public ITextComponent name() {
            return new StringTextComponent("Finish Dialogue");
        }
    }
}
