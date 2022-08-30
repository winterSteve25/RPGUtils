package wintersteve25.rpgutils.common.data.loaded.npc;

import com.google.gson.JsonElement;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.PacketBufferUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NPCDatumType<T> {

    private static final List<NPCDatumType<?>> DATUM_TYPES = new ArrayList<>();

    public static final NPCDatumType<String> NAME = new NPCDatumType<>("name") {
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
    };

    public static final NPCDatumType<String> TEXTURE = new NPCDatumType<>("texture") {
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
    };

    public static final NPCDatumType<Map<Attribute, Double>> ATTRIBUTES = new NPCDatumType<>("attributes") {
        @Override
        public Map<Attribute, Double> deserialiseJson(JsonElement json) {
            Map<String, Double> stringMap = JsonUtilities.gson.fromJson(json, Map.class);
            Map<Attribute, Double> map = new HashMap<>();
            stringMap.forEach((k, v) -> map.put(NPCTypeLoader.ATTRIBUTES.get(k), v));
            return map;
        }

        @Override
        public Map<Attribute, Double> deserialisePacket(PacketBuffer buffer) {
            return PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.ATTRIBUTES.get(buffer.readUtf()), buffer::readDouble);
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Map<Attribute, Double> value) {
            PacketBufferUtils.writeMap(value, buffer, a -> buffer.writeUtf(NPCTypeLoader.ATTRIBUTES.get(a)), buffer::writeDouble);
        }
    };

    public static final NPCDatumType<Map<ModGoals.GoalConstructor, Integer>> GOALS = new NPCDatumType<>("goals") {
        @Override
        public Map<ModGoals.GoalConstructor, Integer> deserialiseJson(JsonElement json) {
            Map<String, Double> stringMap = JsonUtilities.gson.fromJson(json, Map.class);
            Map<ModGoals.GoalConstructor, Integer> map = new HashMap<>();
            stringMap.forEach((k, v) -> map.put(NPCTypeLoader.MOD_GOALS.get(k), v.intValue()));
            return map;
        }

        @Override
        public Map<ModGoals.GoalConstructor, Integer> deserialisePacket(PacketBuffer buffer) {
            return PacketBufferUtils.readMap(buffer, () -> NPCTypeLoader.MOD_GOALS.get(buffer.readUtf()), buffer::readInt);
        }

        @Override
        public void serialisePacket(PacketBuffer buffer, Map<ModGoals.GoalConstructor, Integer> value) {
            PacketBufferUtils.writeMap(value, buffer, g -> buffer.writeUtf(NPCTypeLoader.MOD_GOALS.get(g)), buffer::writeInt);
        }
    };

    public final String jsonName;

    protected NPCDatumType(String jsonName) {
        this.jsonName = jsonName;
    }

    public abstract T deserialiseJson(JsonElement json);

    public abstract T deserialisePacket(PacketBuffer buffer);

    public abstract void serialisePacket(PacketBuffer buffer, T value);

    private static void register(NPCDatumType<?> datumType) {
        DATUM_TYPES.add(datumType);
    }

    public static List<NPCDatumType<?>> getDatumTypes() {
        return DATUM_TYPES;
    }

    static {
        register(NAME);
        register(TEXTURE);
        register(ATTRIBUTES);
        register(GOALS);
    }
}
