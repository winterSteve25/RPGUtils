package wintersteve25.rpgutils.common.quest.dialogue_pool;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialoguePoolManager extends JsonReloadListener {
    public static final DialoguePoolManager INSTANCE = new DialoguePoolManager();

    private final Map<ResourceLocation, List<DialogueRule>> pools = new HashMap<>();

    public DialoguePoolManager() {
        super(new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), "dialogue_pools");
    }

    public Map<ResourceLocation, List<DialogueRule>> getPools() {
        return pools;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager, IProfiler pProfiler) {
        pools.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                List<DialogueRule> rules = new ArrayList<>();
                
                JsonArray pool = entry.getValue().getAsJsonArray();
                for (JsonElement element : pool) {
                    rules.add(DialogueRule.fromJson(element.getAsJsonObject()));
                }
                
                pools.put(resourcelocation, rules);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading dialogue pool {}", resourcelocation, jsonparseexception);
            }
        }
    }
}
