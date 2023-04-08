package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialoguePoolManager extends JsonDataLoader {
    public static final DialoguePoolManager INSTANCE = new DialoguePoolManager();

    private final Map<String, List<DialogueRule>> pools = new HashMap<>();

    public DialoguePoolManager() {
        super("dialogue_pools");
    }

    public Map<String, List<DialogueRule>> getPools() {
        return pools;
    }

    @Override
    protected void apply(Map<String, JsonElement> pObject) {
        RPGUtils.LOGGER.info("Loading dialogue pools");

        pools.clear();
        
        for (Map.Entry<String, JsonElement> entry : pObject.entrySet()) {
            String id = entry.getKey();

            if (id.startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                List<DialogueRule> rules = new ArrayList<>();
                
                JsonArray pool = entry.getValue().getAsJsonArray();
                for (JsonElement element : pool) {
                    rules.add(DialogueRule.fromJson(element.getAsJsonObject()));
                }
                
                pools.put(id, rules);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading dialogue pool {}", id, jsonparseexception);
            }
        }
    }
}
