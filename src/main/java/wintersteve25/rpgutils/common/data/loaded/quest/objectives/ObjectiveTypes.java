package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveTypes {
    public static final Map<String, IObjectiveType<? extends IObjective>> TYPES;

    static {
        TYPES = new HashMap<>();
        TYPES.put("interactBlock", new InteractWithBlockObjective.Type());
        TYPES.put("finishDialogue", new FinishDialogueObjective.Type());
        TYPES.put("interactEntity", new InteractWithEntityObjective.Type());
    }
}
