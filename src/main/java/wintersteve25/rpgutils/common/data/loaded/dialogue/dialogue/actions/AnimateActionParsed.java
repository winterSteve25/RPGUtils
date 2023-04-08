package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueSpeaker;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

import java.util.List;

public class AnimateActionParsed implements ParsedDialogueAction {
    
    private DialogueSpeaker target;
    private String animation;

    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return null;
    }
}