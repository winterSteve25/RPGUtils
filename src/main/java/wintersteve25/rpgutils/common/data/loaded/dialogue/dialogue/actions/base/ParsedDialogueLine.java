package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

public interface ParsedDialogueLine {
    RuntimeDialogueAction createRuntime(DialogueContext context);
}