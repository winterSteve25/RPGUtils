package wintersteve25.rpgutils.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
}
