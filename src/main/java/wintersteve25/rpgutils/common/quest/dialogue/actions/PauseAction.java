package wintersteve25.rpgutils.common.quest.dialogue.actions;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.DialogueUI;
import wintersteve25.rpgutils.common.quest.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

public class PauseAction implements IDialogueAction {
    private final float pause;
    private final boolean skippable;

    private long startPauseMills;
    private boolean skip;
    
    public PauseAction(float pause, boolean skippable) {
        this.pause = pause;
        this.skippable = skippable;
    }

    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        if (skip) return true;
        float timeElapsed = (System.currentTimeMillis() - startPauseMills) / 1000f;
        return timeElapsed >= pause;
    }

    @Override
    public void skip() {
        if (!skippable) return;
        skip = true;
    }

    @Override
    public void initialize(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft) {
        startPauseMills = System.currentTimeMillis();
        skip = false;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "pause");
        object.addProperty("time", pause);
        object.addProperty("skippable", skippable);
        return object;
    }

    public static class Deserializer implements IDeserializer<IDialogueAction> {
        @Override
        public IDialogueAction fromJson(JsonObject jsonObject) {
            return new PauseAction(
                    JsonUtilities.getOrDefault(jsonObject, "time", 1f),
                    JsonUtilities.getOrDefault(jsonObject, "skippable", false)
            );
        }
    }
}
