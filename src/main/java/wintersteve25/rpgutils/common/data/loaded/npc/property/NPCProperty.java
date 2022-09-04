package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Static instances of this class should be created in their subclass to avoid class-loading deadlock
 * @param <T> The type of object that this property stores
 */
public abstract class NPCProperty<T> {

    private static final List<NPCProperty<?>> PROPERTIES = new ArrayList<>();

    public final String jsonName;
    @Nullable
    public final String group;

    protected NPCProperty(String jsonName, @Nullable String group) {
        this.jsonName = jsonName;
        this.group = group;
    }

    public abstract T deserialiseJson(JsonElement json);

    public abstract T deserialisePacket(PacketBuffer buffer);

    public abstract void serialisePacket(PacketBuffer buffer, T value);

    protected static void register(NPCProperty<?> property) {
        if (!(property.group == null)) {
            NPCPropertyGroup group = getGroup(property.group);
            if (group == null) {
                throw new NoSuchGroupException(property.group);
            }
            group.addChild(property);
        } else {
            PROPERTIES.add(property);
        }
    }

    public static NPCPropertyGroup getGroup(String name) {
        for (NPCProperty<?> property : PROPERTIES) {
            if (property instanceof NPCPropertyGroup) {
                if (property.jsonName.equals(name)) {
                    return (NPCPropertyGroup) property;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public static List<NPCProperty<?>> getProperties() {
        return PROPERTIES;
    }

    /**
     * Property groups must be registered first (see getGroup(String))
     */
    public static void register() {
        register(NPCPropertyGroup.DIMENSIONS);
        register(NPCPropertyGroup.SOUNDS);

        register(StringNPCProperty.NAME);
        register(StringNPCProperty.TEXTURE);
        register(StringNPCProperty.MODEL);
        register(StringNPCProperty.ANIMATIONS);
        register(MapNPCProperty.ATTRIBUTES);
        register(MapNPCProperty.GOALS);

        register(SoundEventNPCProperty.AMBIENT_SOUND);
        register(SoundEventNPCProperty.HURT_SOUND);
        register(SoundEventNPCProperty.DEATH_SOUND);

        register(FloatNPCProperty.WIDTH);
        register(FloatNPCProperty.HEIGHT);
        register(FloatNPCProperty.EYE_HEIGHT);
        register(FloatNPCProperty.HELD_ITEM_OFFSET_X);
        register(FloatNPCProperty.HELD_ITEM_OFFSET_Y);

        register(EnumNPCProperty.ITEM_RENDER_TYPE);
    }
}
