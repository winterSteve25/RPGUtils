package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class FloatNPCProperty extends NPCProperty<Float> {

    public static final FloatNPCProperty WIDTH = new FloatNPCProperty("width", "dimensions");
    public static final FloatNPCProperty HEIGHT = new FloatNPCProperty("height", "dimensions");
    public static final FloatNPCProperty EYE_HEIGHT = new FloatNPCProperty("eyeHeight", "dimensions");
    public static final FloatNPCProperty HELD_ITEM_OFFSET_X = new FloatNPCProperty("heldItemOffsetX", "dimensions");
    public static final FloatNPCProperty HELD_ITEM_OFFSET_Y = new FloatNPCProperty("heldItemOffsetY", "dimensions");

    protected FloatNPCProperty(String jsonName, String group) {
        super(jsonName, group);
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
}
