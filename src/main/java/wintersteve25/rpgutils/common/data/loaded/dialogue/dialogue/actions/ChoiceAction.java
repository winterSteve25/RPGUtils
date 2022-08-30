package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;

// TODO
public class ChoiceAction implements IDialogueAction {
    
    
    
    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        return false;
    }

    @Override
    public JsonObject toJson() {
        return null;
    }

    @Override
    public Object[] data() {
        return new Object[0];
    }

    @Override
    public int guiIndex() {
        return 0;
    }
    
    public static class Deserializer implements IDeserializer<ChoiceAction> {

        @Override
        public ChoiceAction fromJson(JsonObject jsonObject) {
            return new ChoiceAction();
        }
    }
}