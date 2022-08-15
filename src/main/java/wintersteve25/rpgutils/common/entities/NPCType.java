package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;

import java.util.HashMap;
import java.util.Map;

public class NPCType {

    public static final String DEFAULT_TEXTURE = "textures/entity/npc/npc.png";

    private final Map<Attribute, Double> attributes;
    private final String name;
    private final String texturePath;

    public NPCType(Map<Attribute, Double> attributes, String name, String texturePath) {
        this.attributes = attributes;
        this.name = name;
        this.texturePath = texturePath;
    }

    public NPCType(PacketBuffer buffer) {
        attributes = new HashMap<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            Attribute attribute = NPCTypeLoader.ATTRIBUTE_NAMES.get(buffer.readUtf());
            double value = buffer.readDouble();
            attributes.put(attribute, value);
        }

        name = buffer.readUtf();
        texturePath = buffer.readUtf();
    }

    public void writeToBuffer(PacketBuffer buffer) {
        buffer.writeInt(attributes.size());
        for (Map.Entry<Attribute, Double> entry : attributes.entrySet()) {
            buffer.writeUtf(NPCTypeLoader.ATTRIBUTE_NAMES.inverse().get(entry.getKey()));
            buffer.writeDouble(entry.getValue());
        }
        buffer.writeUtf(name);
        buffer.writeUtf(texturePath);
    }

    public Map<Attribute, Double> attributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return texturePath;
    }
}
