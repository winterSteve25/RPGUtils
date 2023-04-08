package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.DataLoader;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager extends DataLoader<Dialogue> {
    public static final DialogueManager INSTANCE = new DialogueManager();
    private static final DialogueParser PARSER = new DialogueParser();
    
    private final Map<String, Dialogue> dialogues = new HashMap<>();

    public DialogueManager() {
        super("dialogues", "dg");
    }

    public Map<String, Dialogue> getDialogues() {
        return dialogues;
    }
    
    @Override
    protected void apply(Map<String, Dialogue> data) {
        RPGUtils.LOGGER.info("Loading dialogues");

        dialogues.clear();

        for (Map.Entry<String, Dialogue> entry : data.entrySet()) {
            String id = entry.getKey();
            if (id.startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            dialogues.put(id, entry.getValue());
        }
    }

    @Override
    protected Dialogue map(String stringContent) {
        return PARSER.parse(stringContent);
    }
}
