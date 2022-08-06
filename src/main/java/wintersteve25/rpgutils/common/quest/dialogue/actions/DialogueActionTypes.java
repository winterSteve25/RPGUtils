package wintersteve25.rpgutils.common.quest.dialogue.actions;

import wintersteve25.rpgutils.common.utils.ISerializer;

import java.util.HashMap;
import java.util.Map;

public class DialogueActionTypes {
    public static final Map<String, ISerializer<IDialogueAction>> SERIALIZERS;

    static {
        SERIALIZERS = new HashMap<>();
        SERIALIZERS.put("speak", new SpeakAction.Serializer());
    }
}
