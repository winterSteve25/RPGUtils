package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool;

import java.util.List;

public class DialoguePool {
    private final String leadingSpeaker;
    private final List<DialogueRule> rules;

    public DialoguePool(String leadingSpeaker, List<DialogueRule> rules) {
        this.leadingSpeaker = leadingSpeaker;
        this.rules = rules;
    }

    public String getLeadingSpeaker() {
        return leadingSpeaker;
    }

    public List<DialogueRule> getRules() {
        return rules;
    }
}
