package wintersteve25.rpgutils.common.data.loaded.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;
import wintersteve25.rpgutils.common.data.loaded.npc.property.MapNPCProperty;
import wintersteve25.rpgutils.common.data.loaded.npc.property.NPCProperty;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.entities.NPCType;
import wintersteve25.rpgutils.common.utils.JsonRegistryMap;

import java.util.HashMap;
import java.util.Map;

public class NPCTypeLoader extends JsonDataLoader {

    public static final NPCTypeLoader INSTANCE = new NPCTypeLoader();

    // JSON registry maps
    public static final JsonRegistryMap<Attribute> ATTRIBUTES = new JsonRegistryMap<>(Attributes.class, Attribute.class);
    public static final JsonRegistryMap<ModGoals.GoalConstructor> MOD_GOALS = new JsonRegistryMap<>(ModGoals.class, ModGoals.GoalConstructor.class);
    public static final JsonRegistryMap<SoundEvent> SOUND_EVENTS = new JsonRegistryMap<>(SoundEvents.class, SoundEvent.class);

    private final Map<String, NPCType> typeMap = new HashMap<>();

    private NPCTypeLoader() {
        super("npc");
    }

    @Override
    protected void apply(Map<String, JsonElement> data) {
        RPGUtils.LOGGER.info("Loading NPC attributes");

        NPCProperty.register();

        typeMap.clear();

        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
            String id = entry.getKey();
            
            if (id.startsWith("_")) {
                continue; // Forge: filter anything beginning with "_" as it's used for metadata.
            }
            try {
                JsonObject root = entry.getValue().getAsJsonObject();
                NPCType type = new NPCType(root);
                typeMap.put(id, type);
            } catch (IllegalArgumentException | JsonParseException e) {
                RPGUtils.LOGGER.error("Parsing error loading NPC attribute set {}", id, e);
            }
        }
    }

    public NPCType getType(String name) {
        return typeMap.get(name);
    }

    public AttributeModifierMap createDefaultAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_SPEED, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3).build();
    }

    public void setAttributes(MobEntity entity, String name) {
        Map<Attribute, Double> attributeMap = (Map<Attribute, Double>) typeMap.get(name).getProperty(MapNPCProperty.ATTRIBUTES);
        for (Attribute attribute : ATTRIBUTES.objectSet()) {
            if (attributeMap.containsKey(attribute)) {
                setAttribute(entity, attribute, attributeMap.get(attribute));
            }
        }
        entity.setHealth(entity.getMaxHealth());
    }

    private static void setAttribute(Entity entity, Attribute attribute, double value) {
        ModifiableAttributeInstance attributeInstance = ((LivingEntity) entity).getAttributes().getInstance(attribute);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(value);
        } else {
            RPGUtils.LOGGER.warn("Failed to set attribute {} because attributeInstance was null", attribute.getRegistryName());
        }
    }

    private static Map<Attribute, Double> createAttributes(JsonObject jsonObject) {
        Map<Attribute, Double> attributes = new HashMap<>();
        for (JsonRegistryMap<Attribute>.Entry attribute : ATTRIBUTES.entrySet()) {
            if (jsonObject.has(attribute.getFieldName())) {
                attributes.put(attribute.getObject(), jsonObject.get(attribute.getFieldName()).getAsDouble());
            }
        }
        return attributes;
    }
}
