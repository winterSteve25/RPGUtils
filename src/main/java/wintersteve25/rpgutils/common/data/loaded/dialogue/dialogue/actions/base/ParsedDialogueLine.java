package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

import javax.annotation.Nullable;

public interface ParsedDialogueLine {
    RuntimeDialogueAction createRuntime(DialogueContext context);
}