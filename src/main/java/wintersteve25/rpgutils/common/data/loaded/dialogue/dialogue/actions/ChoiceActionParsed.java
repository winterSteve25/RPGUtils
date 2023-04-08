package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueParser;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.UnsupportedParameter;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

import java.util.ArrayList;
import java.util.List;

public class ChoiceActionParsed implements ParsedDialogueAction {
    private static final DialogueParser RESPONSE_DIALOGUE_PARSER = new DialogueParser();
    
    private final List<Response> responses;
    
    public ChoiceActionParsed() {
        responses = new ArrayList<>();
    }
    
    public List<Response> getResponses() {
        return responses;
    }

    @Override
    public void addListEntry(String value) {
        responses.add(new Response(value));
    }
    
    @Override
    public void addEntryParameters(List<String> parameters) {
        responses.get(responses.size() - 1).result = RESPONSE_DIALOGUE_PARSER.parse(parameters);
    }

    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return null;
    }

    public static class Response {
        private final String message;
        private Dialogue result;

        public Response(String message) {
            this.message = message;
        }
    }
}
