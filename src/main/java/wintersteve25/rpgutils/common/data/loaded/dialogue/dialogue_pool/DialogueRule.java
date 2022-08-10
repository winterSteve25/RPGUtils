package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

public class DialogueRule {
    
    private final float weight;
    private final boolean interruptable;
    private final ResourceLocation dialogue;

    public DialogueRule(float weight, boolean interruptable, ResourceLocation dialogue) {
        this.weight = weight;
        this.interruptable = interruptable;
        this.dialogue = dialogue;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isInterruptable() {
        return interruptable;
    }

    public ResourceLocation getDialogue() {
        return dialogue;
    }

    public static DialogueRule fromJson(JsonObject jsonObject) {
        return new DialogueRule(
                JsonUtilities.getOrDefault(jsonObject, "weight", 1f),
                JsonUtilities.getOrDefault(jsonObject, "interruptable", true),
                new ResourceLocation(jsonObject.get("dialogue").getAsString())
        );
    }
}