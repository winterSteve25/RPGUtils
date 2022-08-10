package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager extends JsonDataLoader {
    public static final DialogueManager INSTANCE = new DialogueManager();

    private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();

    public DialogueManager() {
        super("dialogues");
    }

    public Map<ResourceLocation, Dialogue> getDialogues() {
        return dialogues;
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject) {
        RPGUtils.LOGGER.info("Loading dialogues");
        
        dialogues.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                Dialogue dialogue = Dialogue.fromJson(resourcelocation, entry.getValue());
                dialogues.put(resourcelocation, dialogue);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading recipe {}", resourcelocation, jsonparseexception);
            }
        }
    }
}
