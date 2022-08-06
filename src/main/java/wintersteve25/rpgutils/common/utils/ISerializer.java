package wintersteve25.rpgutils.common.utils;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface ISerializer<T> {
    T fromJson(JsonObject jsonObject);
}
