package wintersteve25.rpgutils.common.data.loaded.npc.datum_type;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public abstract class NPCDatumType<T> {

    private static final List<NPCDatumType<?>> DATUM_TYPES = new ArrayList<>();

    public final String jsonName;

    protected NPCDatumType(String jsonName) {
        this.jsonName = jsonName;
    }

    public abstract T deserialiseJson(JsonElement json);

    public abstract T deserialisePacket(PacketBuffer buffer);

    public abstract void serialisePacket(PacketBuffer buffer, T value);

    protected static void register(NPCDatumType<?> datumType) {
        DATUM_TYPES.add(datumType);
    }

    public static List<NPCDatumType<?>> getDatumTypes() {
        return DATUM_TYPES;
    }

    public static void register() {
        StringNPCDatumType.register();
        MapNPCDatumType.register();
        SoundEventNPCDatumType.register();
    }
}
