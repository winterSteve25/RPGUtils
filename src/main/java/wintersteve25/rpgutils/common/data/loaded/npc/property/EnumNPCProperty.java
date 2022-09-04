package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.client.renderers.npc.HeldItemRenderLayer;

import java.util.function.Function;

public class EnumNPCProperty<T extends Enum<T>> extends NPCProperty<T> {

    public static final EnumNPCProperty<HeldItemRenderLayer.RenderType> ITEM_RENDER_TYPE = new EnumNPCProperty<>("itemRenderType", null, HeldItemRenderLayer.RenderType::valueOf, HeldItemRenderLayer.RenderType::toString);

    private final Function<String, T> enumDecoder;
    private final Function<T, String> enumEncoder;

    protected EnumNPCProperty(String jsonName, String group, Function<String, T> enumDecoder, Function<T, String> enumEncoder) {
        super(jsonName, group);
        this.enumDecoder = enumDecoder;
        this.enumEncoder = enumEncoder;
    }

    @Override
    public T deserialiseJson(JsonElement json) {
        return enumDecoder.apply(json.getAsString());
    }

    @Override
    public T deserialisePacket(PacketBuffer buffer) {
        return enumDecoder.apply(buffer.readUtf());
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, T value) {
        buffer.writeUtf(enumEncoder.apply(value));
    }
}
