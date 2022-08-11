package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types;

import net.minecraft.client.resources.I18n;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.pause.PauseDialogueActionTypeGui;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.spawn.SpawnDialogueActionTypeGui;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak.SpeakDialogueActionTypeGui;

import java.util.function.Supplier;

public enum DialogueActionType {
    SPEAK("rpgutils.dialogues.action_types.speak", SpeakDialogueActionTypeGui::new),
    CLEAR("rpgutils.dialogues.action_types.clear", ClearDialogueActionTypeGui::new),
    PAUSE("rpgutils.dialogues.action_types.pause", PauseDialogueActionTypeGui::new),
    SPAWN("rpgutils.dialogues.action_types.pause", SpawnDialogueActionTypeGui::new);
    
    private final String id;
    private final Supplier<? extends IDialogueActionTypeGui> guiCreator;

    DialogueActionType(String id, Supplier<? extends IDialogueActionTypeGui> guiCreator) {
        this.id = I18n.get(id);
        this.guiCreator = guiCreator;
    }
    
    public Supplier<? extends IDialogueActionTypeGui> getGuiCreator() {
        return guiCreator;
    }

    @Override
    public String toString() {
        return id;
    }
}
