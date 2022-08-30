package wintersteve25.rpgutils.common.entities;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCDatumType;

import java.util.HashMap;
import java.util.Map;

public class NPCType {

    public static final String DEFAULT_TEXTURE = "textures/entity/npc/npc.png";

    private final Map<NPCDatumType<?>, Object> data;

    public NPCType(PacketBuffer buffer) {
        data = new HashMap<>();
        for (NPCDatumType<?> datumType : NPCDatumType.getDatumTypes()) {
            data.put(datumType, datumType.deserialisePacket(buffer));
        }
    }

    public NPCType(JsonObject json) {
        data = new HashMap<>();
        for (NPCDatumType<?> datumType : NPCDatumType.getDatumTypes()) {
            data.put(datumType, datumType.deserialiseJson(json.get(datumType.jsonName)));
        }
    }

    public void writeToBuffer(PacketBuffer buffer) {
        for (NPCDatumType datumType : NPCDatumType.getDatumTypes()) {
            datumType.serialisePacket(buffer, data.get(datumType));
        }
    }

    public <T> T getDatum(NPCDatumType<T> key) {
        return (T) data.get(key);
    }
}
