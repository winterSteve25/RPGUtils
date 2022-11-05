package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class FloatNPCProperty extends NPCProperty<Float> {

    public static final FloatNPCProperty WIDTH = new FloatNPCProperty("width", "dimensions", 1f);
    public static final FloatNPCProperty HEIGHT = new FloatNPCProperty("height", "dimensions", 1.6f);
    public static final FloatNPCProperty EYE_HEIGHT = new FloatNPCProperty("eyeHeight", "dimensions", 1.4f);
    public static final FloatNPCProperty HELD_ITEM_OFFSET_RIGHT = new FloatNPCProperty("heldItemOffsetRight", "heldItem", 7f);
    public static final FloatNPCProperty HELD_ITEM_OFFSET_FORWARD = new FloatNPCProperty("heldItemOffsetForward", "heldItem", 0f);
    public static final FloatNPCProperty HELD_ITEM_OFFSET_UP = new FloatNPCProperty("heldItemOffsetUp", "heldItem", 0.625f);

    protected FloatNPCProperty(String jsonName, String group, float defaultValue) {
        super(jsonName, group, defaultValue);
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
