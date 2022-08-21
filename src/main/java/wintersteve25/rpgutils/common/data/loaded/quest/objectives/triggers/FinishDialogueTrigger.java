package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.util.ResourceLocation;

public class FinishDialogueTrigger {
    private final ResourceLocation dialogue;

    public FinishDialogueTrigger(ResourceLocation dialogue) {
        this.dialogue = dialogue;
    }

    public ResourceLocation getDialogue() {
        return dialogue;
    }
}
