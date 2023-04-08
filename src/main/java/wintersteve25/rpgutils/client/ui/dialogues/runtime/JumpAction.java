package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

public class JumpAction implements RuntimeDialogueAction {

    private final String newDialogue;
    private final DialogueContext dialogueContext;
    
    public JumpAction(String newDialogue, DialogueContext dialogueContext) {
        this.newDialogue = newDialogue;
        this.dialogueContext = dialogueContext;
    }

    @Override
    public void execute(DialogueUI ui) {
        DialogueSystem.play(dialogueContext.player, newDialogue);
    }

    @Override
    public boolean isComplete(DialogueUI ui) {
        return true;
    }
}
