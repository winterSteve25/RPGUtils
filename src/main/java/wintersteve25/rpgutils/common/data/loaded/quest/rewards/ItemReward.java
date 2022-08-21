package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class ItemReward implements IReward {
    
    private final ItemStack item;

    public ItemReward(ItemStack item) {
        this.item = item;
    }

    @Override
    public void giveReward(PlayerEntity player) {
        player.addItem(item);
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", "item");
        jsonObject.add("item", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, item).result().get());
        
        return jsonObject;
    }

    public static class Deserializer implements IDeserializer<IReward> {
        @Override
        public IReward fromJson(JsonObject jsonObject) {
            return new ItemReward(ItemStack.CODEC.parse(JsonOps.INSTANCE, jsonObject.getAsJsonObject("item")).result().get());
        }
    }
}
