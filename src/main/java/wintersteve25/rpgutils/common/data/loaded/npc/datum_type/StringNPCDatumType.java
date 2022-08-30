package wintersteve25.rpgutils.common.data.loaded.npc.datum_type;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class StringNPCDatumType extends NPCDatumType<String> {

    public static final StringNPCDatumType NAME = new StringNPCDatumType("name");
    public static final StringNPCDatumType TEXTURE = new StringNPCDatumType("texture");

    protected StringNPCDatumType(String jsonName) {
        super(jsonName);
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

    public static void register() {
        register(NAME);
        register(TEXTURE);
    }
}
