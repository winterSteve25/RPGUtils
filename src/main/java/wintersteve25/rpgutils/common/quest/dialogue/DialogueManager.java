package wintersteve25.rpgutils.common.quest.dialogue;

import com.google.gson.*;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager extends JsonReloadListener {
    public static final DialogueManager INSTANCE = new DialogueManager();

    private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();

    public DialogueManager() {
        super(new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), "dialogues");
    }

    public Map<ResourceLocation, Dialogue> getDialogues() {
        return dialogues;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager, IProfiler pProfiler) {
        dialogues.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.
            try {
                Dialogue dialogue = Dialogue.fromJson(JSONUtils.convertToJsonObject(entry.getValue(), "top element"));
                dialogues.put(resourcelocation, dialogue);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                RPGUtils.LOGGER.error("Parsing error loading recipe {}", resourcelocation, jsonparseexception);
            }
        }
    }
}
