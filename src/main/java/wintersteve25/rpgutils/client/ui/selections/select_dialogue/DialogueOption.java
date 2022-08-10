package wintersteve25.rpgutils.client.ui.selections.select_dialogue;

import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;

public class DialogueOption extends SelectionOption<DialogueOption> {
    private final Dialogue dialogue;
    
    public DialogueOption(int x, int y, SelectDialogue parent, int index, Dialogue dialogue) {
        super(x, y, dialogue.getResourceLocation().toString(), parent, index);
        this.dialogue = dialogue;
    }
    
    public DialogueOption(DialogueOption copyFrom) {
        super(copyFrom);
        this.dialogue = copyFrom.dialogue;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }
}