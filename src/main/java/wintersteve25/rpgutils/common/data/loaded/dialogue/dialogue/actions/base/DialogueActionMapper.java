package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DialogueActionMapper {
    private final static Map<String, Supplier<ParsedDialogueAction>> MAPPER = new HashMap<>();

    static {
        MAPPER.put("choice", ChoiceActionParsed::new);
        MAPPER.put("animate", AnimateActionParsed::new);
        MAPPER.put("trigger", TriggerActionParsed::new);
        MAPPER.put("camera", CameraActionParsed::new);
        MAPPER.put("jump", JumpActionParsed::new);
    }

    public static ParsedDialogueAction create(String name) {
        if (!MAPPER.containsKey(name)) {
            throw new RuntimeException("Action `" + name + "` is not valid"); 
        }
        
        return MAPPER.get(name).get();
    }
}
