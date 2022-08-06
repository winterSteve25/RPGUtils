package wintersteve25.rpgutils.common.quest.dialogue.actions.base;

import wintersteve25.rpgutils.common.quest.dialogue.actions.ClearAction;
import wintersteve25.rpgutils.common.quest.dialogue.actions.PauseAction;
import wintersteve25.rpgutils.common.quest.dialogue.actions.speak.SpeakAction;
import wintersteve25.rpgutils.common.utils.ISerializer;

import java.util.HashMap;
import java.util.Map;

public class DialogueActionTypes {
    public static final Map<String, ISerializer<IDialogueAction>> SERIALIZERS;

    static {
        SERIALIZERS = new HashMap<>();
        SERIALIZERS.put("speak", new SpeakAction.Serializer());
        SERIALIZERS.put("pause", new PauseAction.Serializer());
        SERIALIZERS.put("clear", json -> new ClearAction());
    }
}
