package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.ClearAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.PauseAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.SpawnAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.speak.SpeakAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.HashMap;
import java.util.Map;

public class DialogueActionTypes {
    public static final Map<String, IDeserializer<? extends IDialogueAction>> DESERIALIZERS;

    static {
        DESERIALIZERS = new HashMap<>();
        DESERIALIZERS.put("speak", new SpeakAction.Deserializer());
        DESERIALIZERS.put("pause", new PauseAction.Deserializer());
        DESERIALIZERS.put("clear", json -> new ClearAction());
        DESERIALIZERS.put("spawn", new SpawnAction.Deserializer());
    }
}
