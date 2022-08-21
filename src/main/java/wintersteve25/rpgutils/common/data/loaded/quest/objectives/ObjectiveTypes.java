package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveTypes {
    public static final Map<String, IDeserializer<? extends IObjective>> DESERIALIZERS;

    static {
        DESERIALIZERS = new HashMap<>();
        DESERIALIZERS.put("interactBlock", new InteractWithBlockObjective.Deserializer());
        DESERIALIZERS.put("interactEntity", new InteractWithEntityObjective.Deserializer());
        DESERIALIZERS.put("finishDialogue", new FinishDialogueObjective.Deserializer());
    }
}
