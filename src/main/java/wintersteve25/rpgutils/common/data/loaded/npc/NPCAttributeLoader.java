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
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;

import java.util.HashMap;
import java.util.Map;

public class NPCAttributeLoader extends JsonDataLoader {

    public static final NPCAttributeLoader INSTANCE = new NPCAttributeLoader();

    private static final Map<Attribute, String> ATTRIBUTE_NAMES;
    static {
        ATTRIBUTE_NAMES = new HashMap<>();
        ATTRIBUTE_NAMES.put(Attributes.MAX_HEALTH, "maxHealth");
        ATTRIBUTE_NAMES.put(Attributes.ATTACK_DAMAGE, "attackDamage");
        ATTRIBUTE_NAMES.put(Attributes.ATTACK_SPEED, "attackSpeed");
        ATTRIBUTE_NAMES.put(Attributes.MOVEMENT_SPEED, "movementSpeed");
    }

    private final Map<String, Map<Attribute, Double>> attributeMap = new HashMap<>();

    public NPCAttributeLoader() {
        super("npc/attributes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data) {
        RPGUtils.LOGGER.info("Loading NPC attributes");

        attributeMap.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : data.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            String path = resourcelocation.getPath();
            if (path.startsWith("_")) {
                continue; // Forge: filter anything beginning with "_" as it's used for metadata.
            }
            try {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                Map<Attribute, Double> attributes = createAttributes(jsonObject);
                attributeMap.put(path, attributes);
            } catch (IllegalArgumentException | JsonParseException e) {
                RPGUtils.LOGGER.error("Parsing error loading NPC attribute set {}", resourcelocation, e);
            }
        }
    }

    public AttributeModifierMap createDefaultAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_SPEED, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3).build();
    }

    public void setAttributes(MobEntity entity, String name) {
        Map<Attribute, Double> modifierMap = attributeMap.get(name);
        for (Attribute attribute : ATTRIBUTE_NAMES.keySet()) {
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
        for (Map.Entry<Attribute, String> attribute : ATTRIBUTE_NAMES.entrySet()) {
            attributes.put(attribute.getKey(), jsonObject.get(attribute.getValue()).getAsDouble());
        }
        return attributes;
    }
}
