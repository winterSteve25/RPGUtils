package wintersteve25.rpgutils.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class JsonUtilities {
    public static String getOrDefault(JsonObject object, String key, String defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsString();
    }

    public static int getOrDefault(JsonObject object, String key, int defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsInt();
    }

    public static float getOrDefault(JsonObject object, String key, float defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsFloat();
    }

    public static boolean getOrDefault(JsonObject object, String key, boolean defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsBoolean();
    }
    
    public static <T> T getOrDefault(JsonObject object, String key, Function<JsonElement, T> mapper, T defaultValue) {
        if (!object.has(key)) return defaultValue;
        return mapper.apply(object.get(key));
    }
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Path generatedPath = FMLPaths.getOrCreateGameRelativePath(Paths.get("rpgutils/generated/dialogues/"), "");
    
    public static void saveDialogue(Object jsonObject) {
        try {
            FileWriter fileWriter = new FileWriter(generatedPath + "/test.json");
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
