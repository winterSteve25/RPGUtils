package wintersteve25.rpgutils.common.utils;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface IDeserializer<T> {
    T fromJson(JsonObject jsonObject);
}
