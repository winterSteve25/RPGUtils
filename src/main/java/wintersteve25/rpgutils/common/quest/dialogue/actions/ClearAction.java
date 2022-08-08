package wintersteve25.rpgutils.common.quest.dialogue.actions;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.DialogueUI;
import wintersteve25.rpgutils.common.quest.dialogue.actions.base.IDialogueAction;

public class ClearAction implements IDialogueAction {
    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        dialogueUI.displayingDialogueText = "";
        return true;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "clear");
        return object;
    }
}
