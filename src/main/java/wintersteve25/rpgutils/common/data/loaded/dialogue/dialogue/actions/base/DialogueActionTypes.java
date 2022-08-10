package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.ClearAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.PauseAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.speak.SpeakAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.HashMap;
import java.util.Map;

public class DialogueActionTypes {
    public static final Map<String, IDeserializer<IDialogueAction>> SERIALIZERS;

    static {
        SERIALIZERS = new HashMap<>();
        SERIALIZERS.put("speak", new SpeakAction.Deserializer());
        SERIALIZERS.put("pause", new PauseAction.Deserializer());
        SERIALIZERS.put("clear", json -> new ClearAction());
    }
}
