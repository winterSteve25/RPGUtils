package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.JumpAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

public class JumpActionParsed implements ParsedDialogueAction {
    
    private String newDialogue;

    @Override
    public void addNormalParameter(String parameter) {
        newDialogue = parameter;
    }

    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return new JumpAction(newDialogue, context);
    }
}
