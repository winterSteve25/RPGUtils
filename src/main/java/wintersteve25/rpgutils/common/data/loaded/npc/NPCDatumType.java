package wintersteve25.rpgutils.common.data.loaded.npc;

import com.google.gson.JsonElement;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.PacketBufferUtils;

import java.util.HashMap;
import java.util.Map;

public enum NPCDatumType {

    NAME("name") {
        @Override
        public Object deserialiseJson(JsonElement json) {
            return json.getAsString();
        }

        @Override
        public Object deserialisePacket(PacketBuffer buffer) {
            return buffer.readUtf();
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Object value) {
            buffer.writeUtf((String) value);
        }
    },

    TEXTURE("texture") {
        @Override
        public Object deserialiseJson(JsonElement json) {
            return json.getAsString();
        }

        @Override
        public Object deserialisePacket(PacketBuffer buffer) {
            return buffer.readUtf();
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Object value) {
            buffer.writeUtf((String) value);
        }
    },

    ATTRIBUTES("attributes") {
        @Override
        public Object deserialiseJson(JsonElement json) {
            Map<String, Double> stringMap = JsonUtilities.gson.fromJson(json, Map.class);
            Map<Attribute, Double> map = new HashMap<>();
            stringMap.forEach((k, v) -> map.put(NPCTypeLoader.ATTRIBUTES.get(k), v));
            return map;
        }

        @Override
        public Object deserialisePacket(PacketBuffer buffer) {
            return PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.ATTRIBUTES.get(buffer.readUtf()), buffer::readDouble);
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Object value) {
            PacketBufferUtils.writeMap((Map<Attribute, Double>) value, buffer, a -> buffer.writeUtf(NPCTypeLoader.ATTRIBUTES.get(a)), buffer::writeDouble);
        }
    },

    GOAL_WEIGHTS("goals") {
        @Override
        public Object deserialiseJson(JsonElement json) {
            Map<String, Double> stringMap = JsonUtilities.gson.fromJson(json, Map.class);
            Map<ModGoals.GoalConstructor, Integer> map = new HashMap<>();
            stringMap.forEach((k, v) -> map.put(NPCTypeLoader.MOD_GOALS.get(k), v.intValue()));
            return map;
        }

        @Override
        public Object deserialisePacket(PacketBuffer buffer) {
            return PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.MOD_GOALS.get(buffer.readUtf()), buffer::readInt);
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Object value) {
            PacketBufferUtils.writeMap((Map<ModGoals.GoalConstructor, Integer>) value, buffer, g -> buffer.writeUtf(NPCTypeLoader.MOD_GOALS.get(g)), buffer::writeInt);
        }
    };

    public final String jsonName;

    NPCDatumType(String jsonName) {
        this.jsonName = jsonName;
    }

    public abstract Object deserialiseJson(JsonElement json);

    public abstract Object deserialisePacket(PacketBuffer buffer);

    public abstract void serialisePacket(PacketBuffer buffer, Object value);
}
