package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.utils.PacketBufferUtils;

import java.util.HashMap;
import java.util.Map;

public class NPCType {

    public static final String DEFAULT_TEXTURE = "textures/entity/npc/npc.png";
    public static final NPCType DEFAULT = new NPCType(new HashMap<>(), "", DEFAULT_TEXTURE, new HashMap<>());

    private final Map<Attribute, Double> attributes;
    private final String name;
    private final String texturePath;
    private final Map<ModGoals.GoalConstructor, Integer> goalWeights;

    public NPCType(Map<Attribute, Double> attributes, String name, String texturePath, Map<ModGoals.GoalConstructor, Integer> goalWeights) {
        this.attributes = attributes;
        this.name = name;
        this.texturePath = texturePath;
        this.goalWeights = goalWeights;
    }

    public NPCType(PacketBuffer buffer) {
        attributes = PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.ATTRIBUTES.get(buffer.readUtf()), buffer::readDouble);
        name = buffer.readUtf();
        texturePath = buffer.readUtf();
        goalWeights = PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.MOD_GOALS.get(buffer.readUtf()), buffer::readInt);
    }

    public void writeToBuffer(PacketBuffer buffer) {
        PacketBufferUtils.writeMap(attributes, buffer, k -> buffer.writeUtf(NPCTypeLoader.ATTRIBUTES.get(k)), buffer::writeDouble);
        buffer.writeUtf(name);
        buffer.writeUtf(texturePath);
        PacketBufferUtils.writeMap(goalWeights, buffer, k -> buffer.writeUtf(NPCTypeLoader.MOD_GOALS.get(k)), buffer::writeInt);
    }

    public Map<Attribute, Double> getAttributes() {
        return attributes;
    }

    public Map<ModGoals.GoalConstructor, Integer> getGoalWeights() {
        return goalWeights;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return texturePath;
    }
}
