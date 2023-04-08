package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.data.temporary.EntityIDMapper;

import java.util.UUID;

public class DialogueSpeaker {

    private final String characterName;
    private final SpeakerType type;
    private UUID uuid;

    public DialogueSpeaker(String character) {
        characterName = character;
        
        if (character.equals("player")) {
            type = SpeakerType.PLAYER;
        } else if (character.split("-").length == 5) {
            type = SpeakerType.FIXED;
            uuid = UUID.fromString(character);
        } else {
            type = SpeakerType.NPC;
        }
    }

    public UUID getUuid(PlayerEntity player) {
        if (uuid == null) {
            if (type == SpeakerType.FIXED) {
                RPGUtils.LOGGER.error("Dynamic UUID with type FIXED does not have a fixed uuid assigned!");
                return null;
            } else if (type == SpeakerType.PLAYER) {
                uuid = player.getUUID();
            } else {
                uuid = NpcIDMapping.clientInstance.get(characterName);
            }
        }

        return uuid;
    }

    public ITextComponent getDisplayableName(PlayerEntity player) {
        if (type == SpeakerType.PLAYER) {
            return player.getDisplayName();
        }

        Entity entity = player.level.getEntity(EntityIDMapper.CLIENT.getEntityId(getUuid(player)));
        if (entity != null) return entity.getDisplayName();
        
        return new StringTextComponent("NOT VALID SPEAKER");
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("characterName", characterName);
        return jsonObject;
    }
    
    public SpeakerType getType()
    {
        return type;
    }
    
    public static DialogueSpeaker fromJson(JsonObject jsonObject) {
        return new DialogueSpeaker(jsonObject.get("characterName").getAsString());
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
                uuid = characterName;
                break;
        }
        
        return type + " - " + uuid;
    }

    public enum SpeakerType {
        FIXED,
        PLAYER,
        NPC
    }
}
