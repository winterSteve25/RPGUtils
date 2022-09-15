package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class StringNPCProperty extends NPCProperty<String> {

    public static final StringNPCProperty NAME = new StringNPCProperty("name", null);
    public static final StringNPCProperty TEXTURE = new StringNPCProperty("texture", null);
    public static final StringNPCProperty MODEL = new StringNPCProperty("model", null);
    public static final StringNPCProperty ANIMATIONS = new StringNPCProperty("animations", null);

    protected StringNPCProperty(String jsonName, String group) {
        super(jsonName, group);
    }

    @Override
    public String deserialiseJson(JsonElement json) {
        return json.getAsString();
    }

    @Override
    public String deserialisePacket(PacketBuffer buffer) {
        return buffer.readUtf();
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, String value) {
        buffer.writeUtf(value);
    }
}
