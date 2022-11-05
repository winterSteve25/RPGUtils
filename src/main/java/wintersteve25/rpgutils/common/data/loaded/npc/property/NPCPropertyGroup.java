package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

import java.util.*;
import java.util.function.Supplier;

public class NPCPropertyGroup extends NPCProperty<Map<String, Object>> {

    public static final NPCPropertyGroup DIMENSIONS = new NPCPropertyGroup("dimensions", null);
    public static final NPCPropertyGroup HELD_ITEM = new NPCPropertyGroup("heldItem", null);
    public static final NPCPropertyGroup SOUNDS = new NPCPropertyGroup("sounds", null);

    private final List<NPCProperty<Object>> children = new ArrayList<>();

    protected NPCPropertyGroup(String jsonName, String group) {
        super(jsonName, group, null);
    }

    @Override
    public Map<String, Object> getDefault() {
        Map<String, Object> map = new HashMap<>();
        for (NPCProperty<Object> child : children) {
            map.put(child.jsonName, child.getDefault());
        }
        return map;
    }

    protected void addChild(NPCProperty<?> child) {
        this.children.add((NPCProperty<Object>) child);
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
}
