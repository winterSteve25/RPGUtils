package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;

public class TypeSpeedAction implements RuntimeDialogueAction {
    
    private final DialogueContext context;
    private final int tickInterval;

    public TypeSpeedAction(DialogueContext context, int tickInterval) {
        this.context = context;
        this.tickInterval = tickInterval;
    }

    @Override
    public void execute(DialogueUI ui) {
        context.typeInterval = tickInterval;
    }

    @Override
    public boolean isComplete(DialogueUI ui) {
        return true;
    }
}
