package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class NPCPropertyGroup extends NPCProperty<Map<String, Object>> {

    private final NPCProperty<Object>[] children;

    @SafeVarargs
    protected NPCPropertyGroup(String jsonName, NPCProperty<Object>... children) {
        super(jsonName);
        this.children = children;
    }

    @Override
    public Map<String, Object> deserialiseJson(JsonElement json) {
        Map<String, Object> data = new HashMap<>();
        for (NPCProperty<?> child : children) {
            data.put(child.jsonName, child.deserialiseJson(json.getAsJsonObject().get(child.jsonName)));
        }
        return data;
    }

    @Override
    public Map<String, Object> deserialisePacket(PacketBuffer buffer) {
        Map<String, Object> data = new HashMap<>();
        for (NPCProperty<?> child : children) {
            data.put(child.jsonName, child.deserialisePacket(buffer));
        }
        return data;
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, Map<String, Object> value) {
        for (NPCProperty<Object> child : children) {
            child.serialisePacket(buffer, value.get(child.jsonName));
        }
    }

    public static void register() {

    }
}
