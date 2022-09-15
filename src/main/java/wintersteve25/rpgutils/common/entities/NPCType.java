package wintersteve25.rpgutils.common.entities;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.property.NPCProperty;
import wintersteve25.rpgutils.common.data.loaded.npc.property.NPCPropertyGroup;
import wintersteve25.rpgutils.common.data.loaded.npc.property.NoSuchGroupException;

import java.util.HashMap;
import java.util.Map;

public class NPCType {

    public static final String DEFAULT_TEXTURE = "textures/entity/npc/npc.png";

    private final Map<NPCProperty<?>, Object> data;

    public NPCType(PacketBuffer buffer) {
        data = new HashMap<>();
        for (NPCProperty<?> datumType : NPCProperty.getProperties()) {
            data.put(datumType, datumType.deserialisePacket(buffer));
        }
    }

    public NPCType(JsonObject json) {
        data = new HashMap<>();
        for (NPCProperty<?> datumType : NPCProperty.getProperties()) {
            data.put(datumType, datumType.deserialiseJson(json.get(datumType.jsonName)));
        }
    }

    public void writeToBuffer(PacketBuffer buffer) {
        for (NPCProperty datumType : NPCProperty.getProperties()) {
            datumType.serialisePacket(buffer, data.get(datumType));
        }
    }

    public Object getProperty(NPCProperty<?> key) {
        if (key.group != null) {
            NPCPropertyGroup group = NPCProperty.getGroup(key.group);
            if (group == null) {
                throw new NoSuchGroupException(key.group);
            }
            return ((HashMap<?, ?>) data.get(group)).get(key.jsonName);
        }
        return data.get(key);
    }
}
