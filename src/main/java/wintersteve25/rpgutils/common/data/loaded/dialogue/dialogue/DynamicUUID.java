package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.RPGUtils;

import java.util.UUID;

public class DynamicUUID {

    private final DynamicType type;
    private UUID uuid;

    public DynamicUUID(DynamicType type) {
        this.type = type;
    }

    public DynamicType getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        if (type == DynamicType.FIXED) {
            RPGUtils.LOGGER.warn("Can not set uuid on a dynamic uuid with type FIXED");
            return;
        }
        
        if (type == DynamicType.PLAYER) {
            RPGUtils.LOGGER.warn("You should not use Dynamic UUID for the player's UUID");
            return;
        }
        
        this.uuid = uuid;
    }

    public enum DynamicType {
        FIXED,
        PLAYER,
        DYNAMIC
    }
}
