package wintersteve25.rpgutils.common.data.loaded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public abstract class JsonDataLoader extends DataLoader<JsonElement> {
    protected static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    
    protected JsonDataLoader(String subdirectory) {
        super(subdirectory, "json");
    }

    @Override
    protected JsonElement map(String stringContent) {
        return GSON.fromJson(stringContent, JsonElement.class);
    }
}
