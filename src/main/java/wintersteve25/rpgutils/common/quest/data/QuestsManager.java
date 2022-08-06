package wintersteve25.rpgutils.common.quest.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;

import java.util.HashMap;
import java.util.Map;

public class QuestsManager extends JsonReloadListener {
    public static final QuestsManager INSTANCE = new QuestsManager();
    
    private final Map<ResourceLocation, Quest> quests = new HashMap<>();
    
    public QuestsManager() {
        super(new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), "quests");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager, IProfiler pProfiler) {
        quests.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                Quest quest = Quest.fromJson(resourcelocation, JSONUtils.convertToJsonObject(entry.getValue(), "top element"));
                quests.put(resourcelocation, quest);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading quest {}", resourcelocation, jsonparseexception);
            }
        }
    }

    public Map<ResourceLocation, Quest> getQuests() {
        return quests;
    }
}
