package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueSpeaker;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueLine;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.SpeakAction;

import java.util.HashMap;
import java.util.Map;

public class CharacterLineParsed implements ParsedDialogueLine {
    private final DialogueSpeaker speaker;
    private final String line;
    private final Map<Integer, ParsedDialogueAction> parseTime;

    public CharacterLineParsed(String speaker, String line, Map<Integer, ParsedDialogueAction> embeddedActions) {
        this.speaker = new DialogueSpeaker(speaker);
        this.line = line;
        this.parseTime = embeddedActions;
    }

    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        Map<Integer, RuntimeDialogueAction> embeddedActions = new HashMap<>();
            
        for (Map.Entry<Integer, ParsedDialogueAction> embed : parseTime.entrySet()) {
            embeddedActions.put(embed.getKey(), embed.getValue().createRuntime(context));
        }
        
        return new SpeakAction(speaker, line, embeddedActions, context);
    }
}
