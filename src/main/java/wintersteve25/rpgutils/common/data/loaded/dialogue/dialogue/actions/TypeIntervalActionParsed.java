package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.TypeSpeedAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;

public class TypeIntervalActionParsed implements ParsedDialogueAction {

    private int tickInterval = 1;
    
    @Override
    public void addNormalParameter(String parameter) {
        if (parameter.endsWith("s")) {
            tickInterval = (int) (Float.parseFloat(parameter.substring(0, parameter.length() - 1)) * 20);
        } else {
            tickInterval = Integer.parseInt(parameter);
        }
    }

    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return new TypeSpeedAction(context, tickInterval);
    }
}
