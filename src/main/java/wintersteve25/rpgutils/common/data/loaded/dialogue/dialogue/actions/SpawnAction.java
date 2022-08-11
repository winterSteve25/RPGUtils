package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.DialogueUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class SpawnAction implements IDialogueAction {
    
    private final ResourceLocation entityType;
    private final BlockPos posToSpawn;
    private final String npcID;

    public SpawnAction(ResourceLocation entityType, BlockPos posToSpawn, String npcID) {
        this.entityType = entityType;
        this.posToSpawn = posToSpawn;
        this.npcID = npcID;
    }

    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        return true;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "spawn");
        object.addProperty("npcID", npcID);
        object.addProperty("entityType", entityType.toString());
        object.add("pos", BlockPos.CODEC.encodeStart(JsonOps.INSTANCE, posToSpawn).result().get());
        return object;
    }
    
    public static class Deserializer implements IDeserializer<SpawnAction> {
        @Override
        public SpawnAction fromJson(JsonObject jsonObject) {
            return new SpawnAction(
                    new ResourceLocation(jsonObject.get("entityType").getAsString()),
                    BlockPos.CODEC.parse(JsonOps.INSTANCE, jsonObject.get("pos")).result().get(),
                    jsonObject.get("npcID").getAsString()
            );
        }
    }
}
