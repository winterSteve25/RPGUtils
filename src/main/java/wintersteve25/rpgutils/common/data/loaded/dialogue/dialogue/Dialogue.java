package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueLine;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

import java.util.*;
import java.util.stream.Collectors;

public class Dialogue {
    private final List<ParsedDialogueLine> lines;

    public Dialogue() {
        lines = new ArrayList<>();
    }

    public void add(ParsedDialogueLine line) {
        this.lines.add(line);
    }

    public Queue<RuntimeDialogueAction> getLines(DialogueContext context) {
        return lines.stream()
                .map(a -> a.createRuntime(context))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
