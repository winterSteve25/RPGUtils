package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;

import java.util.UUID;

public class DynamicUUID {

    private final DynamicType type;
    private UUID uuid;
    private String dynamicNpcID;

    public DynamicUUID(DynamicType type) {
        this.type = type;
    }

    public DynamicType getType() {
        return type;
    }

    public UUID getUuid() {
        if (uuid == null) {
            if (type == DynamicType.FIXED) {
                RPGUtils.LOGGER.warn("Dynamic UUID with type FIXED does not have a fixed uuid assigned!");
                return null;
            } else {
                setup();
                if (type == DynamicType.DYNAMIC) {
                    if (!NpcIDMapping.clientInstance.has(dynamicNpcID)) {
                        RPGUtils.LOGGER.warn("Using a Dynamic DynamicUUID with npcID: {} but no UUID mapped to the npcID", dynamicNpcID);
                        return null;
                    }
                } else {
                    RPGUtils.LOGGER.warn("Local player not found");
                    return null;
                }
            }
        }

        return uuid;
    }

    public void setup() {
        switch (type) {
            case FIXED:
                break;
            case PLAYER:
                uuid = Minecraft.getInstance().player.getUUID();
                break;
            case DYNAMIC:
                uuid = NpcIDMapping.clientInstance.get(dynamicNpcID);
                break;
        }
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDynamicNpcID(String dynamicNpcID) {
        this.dynamicNpcID = dynamicNpcID;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", type.name());
        
        if (type == DynamicType.FIXED) {
            jsonObject.addProperty("uuid", uuid.toString());
        }
        
        if (type == DynamicType.DYNAMIC) {
            jsonObject.addProperty("npcID", dynamicNpcID);
        }
        
        return jsonObject;
    }
    
    public static DynamicUUID fromJson(JsonObject jsonObject) {
        DynamicUUID uuid = new DynamicUUID(DynamicType.valueOf(jsonObject.get("type").getAsString()));

        if (uuid.getType() == DynamicType.FIXED) {
            uuid.setUuid(UUID.fromString(jsonObject.get("uuid").getAsString()));
        }

        if (uuid.getType() == DynamicType.DYNAMIC) {
            uuid.setDynamicNpcID(jsonObject.get("npcID").getAsString());
        }

        return uuid;
    }

    @Override
    public String toString() {
        String uuid;
        
        switch (type) {
            case FIXED:
                uuid = this.uuid.toString();
                break;
            case PLAYER:
                uuid = "Local Player";
                break;
            default:
                uuid = dynamicNpcID;
                break;
        }
        
        return type + " - " + uuid;
    }

    public String getDynamicNpcID() {
        return dynamicNpcID;
    }

    public enum DynamicType {
        FIXED,
        PLAYER,
        DYNAMIC
    }
}
