package wintersteve25.rpgutils.common.quest.dialogue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Tuple;
import wintersteve25.rpgutils.common.quest.dialogue.actions.base.DialogueActionTypes;
import wintersteve25.rpgutils.common.quest.dialogue.actions.base.IDialogueAction;

import java.util.*;

public class Dialogue {
    private final List<Tuple<Optional<UUID>, IDialogueAction>> lines;

    public Dialogue(List<Tuple<Optional<UUID>, IDialogueAction>> lines) {
        this.lines = lines;
    }

    /**
     * @return All the lines in the dialogue. If UUID is not present it means it is the player's line. 
     */
    public List<Tuple<Optional<UUID>, IDialogueAction>> getLines() {
        return lines;
    }

    public static Dialogue fromJson(JsonObject jsonObject) {
        List<Tuple<Optional<UUID>, IDialogueAction>> lines = new ArrayList<>();
        
        JsonArray map = jsonObject.getAsJsonArray("lines");
        for (JsonElement l : map) {
            JsonObject line = l.getAsJsonObject();
            JsonObject action = line.get("action").getAsJsonObject();
            
            String speakerString = line.get("speaker").getAsString();
            UUID uuid;
            
            if (speakerString.equals("PLAYER")) {
                uuid = null;
            } else {
                uuid = UUID.fromString(speakerString);
            }
            
            lines.add(new Tuple<>(Optional.ofNullable(uuid), DialogueActionTypes.SERIALIZERS.get(action.get("type").getAsString()).fromJson(action)));
        }
        
        return new Dialogue(lines);
    }
}