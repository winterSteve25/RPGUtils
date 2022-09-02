package wintersteve25.rpgutils.common.data.loaded.npc.datum_type;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class FloatNPCDatumType extends NPCDatumType<Float> {

    public static final FloatNPCDatumType WIDTH = new FloatNPCDatumType("width");
    public static final FloatNPCDatumType HEIGHT = new FloatNPCDatumType("height");

    protected FloatNPCDatumType(String jsonName) {
        super(jsonName);
    }

    @Override
    public Float deserialiseJson(JsonElement json) {
        return json.getAsFloat();
    }

    @Override
    public Float deserialisePacket(PacketBuffer buffer) {
        return buffer.readFloat();
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, Float value) {
        buffer.writeFloat(value);
    }

    public static void register() {
        register(WIDTH);
        register(HEIGHT);
    }
}
