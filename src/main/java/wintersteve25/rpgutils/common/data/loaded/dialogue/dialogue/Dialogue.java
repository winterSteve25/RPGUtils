package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.DialogueActionTypes;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;

import java.util.*;

public class Dialogue {
    private final List<Tuple<DynamicUUID, IDialogueAction>> lines;
    private final ResourceLocation resourceLocation;
    private final DialoguePredicate predicate;
    
    public Dialogue(ResourceLocation resourceLocation, List<Tuple<DynamicUUID, IDialogueAction>> lines, DialoguePredicate predicate) {
        this.lines = lines;
        this.resourceLocation = resourceLocation;
        this.predicate = predicate;
    }

    /**
     * @return All the lines in the dialogue. If UUID is not present it means it is the player's line. 
     */
    public List<Tuple<DynamicUUID, IDialogueAction>> getLines() {
        return lines;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public boolean isValid() {
        return predicate == null || predicate.test();
    }

    public static Dialogue fromJson(ResourceLocation resourceLocation, JsonElement jsonObject) {
        List<Tuple<DynamicUUID, IDialogueAction>> lines = new ArrayList<>();

        JsonObject object = jsonObject.getAsJsonObject();
        
        JsonArray array = object.get("lines").getAsJsonArray();
        for (JsonElement l : array) {
            JsonObject line = l.getAsJsonObject();
            JsonObject action = line.get("action").getAsJsonObject();
            DynamicUUID uuid = DynamicUUID.fromJson(line.getAsJsonObject("speaker"));
            
            lines.add(new Tuple<>(uuid, DialogueActionTypes.DESERIALIZERS.get(action.get("type").getAsString()).fromJson(action)));
        }

        DialoguePredicate predicate = null;
        
        if (object.has("predicates")) {
            JsonObject predicateJson = object.getAsJsonObject("predicate");
            String predicateName = predicateJson.get("name").getAsString();
            predicate = DialoguePredicate.create(predicateName, predicateJson);
        }
        
        return new Dialogue(resourceLocation, lines, predicate);
    }
}