package wintersteve25.rpgutils.common.data.loaded.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;

import java.util.HashMap;
import java.util.Map;

public class QuestsManager extends JsonDataLoader {
    public static final QuestsManager INSTANCE = new QuestsManager();
    
    private final Map<String, Quest> quests = new HashMap<>();
    
    public QuestsManager() {
        super("quests");
    }
    
    @Override
    protected void apply(Map<String, JsonElement> data) {
        RPGUtils.LOGGER.info("Loading quests");
        
        quests.clear();

        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
            String id = entry.getKey();
            
            if (id.startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                Quest quest = Quest.fromJson(id, JSONUtils.convertToJsonObject(entry.getValue(), "top element"));
                quests.put(id, quest);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading quest {}", id, jsonparseexception);
            }
        }
    }

    public Map<String, Quest> getQuests() {
        return quests;
    }
}
