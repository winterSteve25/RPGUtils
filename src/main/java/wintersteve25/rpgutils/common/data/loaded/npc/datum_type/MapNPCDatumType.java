package wintersteve25.rpgutils.common.data.loaded.npc.datum_type;

import com.google.gson.JsonElement;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.PacketBufferUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <V1> The class of the values in JSON files - must be String or Double
 * @param <K2> The type of the map keys
 * @param <V2> The type of the map values
 */
public abstract class MapNPCDatumType<V1, K2, V2> extends NPCDatumType<Map<K2, V2>> {

    public static final MapNPCDatumType<Double, Attribute, Double> ATTRIBUTES = new MapNPCDatumType<>("attributes") {
        @Override
        protected Attribute fromJsonKey(String key) {
            return NPCTypeLoader.ATTRIBUTES.get(key);
        }

        @Override
        protected Double fromJsonValue(Double value) {
            return value;
        }

        @Override
        protected Attribute deserialiseKey(PacketBuffer buffer) {
            return NPCTypeLoader.ATTRIBUTES.get(buffer.readUtf());
        }

        @Override
        protected Double deserialiseValue(PacketBuffer buffer) {
            return buffer.readDouble();
        }

        @Override
        protected void serialiseKey(Attribute key, PacketBuffer buffer) {
            buffer.writeUtf(NPCTypeLoader.ATTRIBUTES.get(key));
        }

        @Override
        protected void serialiseValue(Double value, PacketBuffer buffer) {
            buffer.writeDouble(value);
        }
    };

    public static final MapNPCDatumType<Double, ModGoals.GoalConstructor, Integer> GOALS = new MapNPCDatumType<>("goals") {
        @Override
        protected ModGoals.GoalConstructor fromJsonKey(String key) {
            return NPCTypeLoader.MOD_GOALS.get(key);
        }

        @Override
        protected Integer fromJsonValue(Double value) {
            return value.intValue();
        }

        @Override
        protected ModGoals.GoalConstructor deserialiseKey(PacketBuffer buffer) {
            return NPCTypeLoader.MOD_GOALS.get(buffer.readUtf());
        }

        @Override
        protected Integer deserialiseValue(PacketBuffer buffer) {
            return buffer.readInt();
        }

        @Override
        protected void serialiseKey(ModGoals.GoalConstructor key, PacketBuffer buffer) {
            buffer.writeUtf(NPCTypeLoader.MOD_GOALS.get(key));
        }

        @Override
        protected void serialiseValue(Integer value, PacketBuffer buffer) {
            buffer.writeInt(value);
        }
    };

    protected MapNPCDatumType(String jsonName) {
        super(jsonName);
    }

    @Override
    public Map<K2, V2> deserialiseJson(JsonElement json) {
        Map<String, V1> stringMap = JsonUtilities.gson.fromJson(json, Map.class);
        Map<K2, V2> map = new HashMap<>();
        stringMap.forEach((k, v) -> map.put(fromJsonKey(k), fromJsonValue(v)));
        return map;
    }

    @Override
    public Map<K2, V2> deserialisePacket(PacketBuffer buffer) {
        return PacketBufferUtils.readMap(buffer, () -> deserialiseKey(buffer), () -> deserialiseValue(buffer));
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, Map<K2, V2> value) {
        PacketBufferUtils.writeMap(value, buffer, (k) -> serialiseKey(k, buffer), (v) -> serialiseValue(v, buffer));
    }

    protected abstract K2 fromJsonKey(String key);

    protected abstract V2 fromJsonValue(V1 value);

    protected abstract K2 deserialiseKey(PacketBuffer buffer);

    protected abstract V2 deserialiseValue(PacketBuffer buffer);

    protected abstract void serialiseKey(K2 key, PacketBuffer buffer);

    protected abstract void serialiseValue(V2 value, PacketBuffer buffer);

    public static void register() {
        register(ATTRIBUTES);
        register(GOALS);
    }
}
