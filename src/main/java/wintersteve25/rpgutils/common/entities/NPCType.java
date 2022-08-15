package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.ai.attributes.Attribute;

import java.util.Map;

public final class NPCType {

    public static final String DEFAULT_TEXTURE = "textures/entity/npc/npc.png";

    private final Map<Attribute, Double> attributes;
    private final String name;
    private final String texturePath;

    public NPCType(Map<Attribute, Double> attributes, String name, String texturePath) {
        this.attributes = attributes;
        this.name = name;
        this.texturePath = texturePath;
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
