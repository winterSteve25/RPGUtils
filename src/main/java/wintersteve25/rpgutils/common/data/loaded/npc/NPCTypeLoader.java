package wintersteve25.rpgutils.common.data.loaded.npc;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;
import wintersteve25.rpgutils.common.entities.NPCType;

import java.util.HashMap;
import java.util.Map;

public class NPCTypeLoader extends JsonDataLoader {

    public static final NPCTypeLoader INSTANCE = new NPCTypeLoader();

    public static final BiMap<String, Attribute> ATTRIBUTE_NAMES;
    static {
        ATTRIBUTE_NAMES = HashBiMap.create();
        ATTRIBUTE_NAMES.put("maxHealth", Attributes.MAX_HEALTH);
        ATTRIBUTE_NAMES.put("attackDamage", Attributes.ATTACK_DAMAGE);
        ATTRIBUTE_NAMES.put("attackSpeed", Attributes.ATTACK_SPEED);
        ATTRIBUTE_NAMES.put("movementSpeed", Attributes.MOVEMENT_SPEED);
    }

    private final Map<String, NPCType> typeMap = new HashMap<>();

    public NPCTypeLoader() {
        super("npc/attributes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data) {
        RPGUtils.LOGGER.info("Loading NPC attributes");

        typeMap.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : data.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            String path = resourcelocation.getPath();
            if (path.startsWith("_")) {
                continue; // Forge: filter anything beginning with "_" as it's used for metadata.
            }
            try {
                JsonObject root = entry.getValue().getAsJsonObject();
                Map<Attribute, Double> attributes = createAttributes(root.getAsJsonObject("attributes"));
                NPCType type = new NPCType(attributes, path, root.get("texture").getAsString());
                typeMap.put(path, type);
            } catch (IllegalArgumentException | JsonParseException e) {
                RPGUtils.LOGGER.error("Parsing error loading NPC attribute set {}", resourcelocation, e);
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
        Map<Attribute, Double> modifierMap = typeMap.get(name).attributes();
        for (Attribute attribute : ATTRIBUTE_NAMES.values()) {
            setAttribute(entity, attribute, modifierMap.get(attribute));
        }
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
        for (Map.Entry<String, Attribute> attribute : ATTRIBUTE_NAMES.entrySet()) {
            attributes.put(attribute.getValue(), jsonObject.get(attribute.getKey()).getAsDouble());
        }
        return attributes;
    }
}
