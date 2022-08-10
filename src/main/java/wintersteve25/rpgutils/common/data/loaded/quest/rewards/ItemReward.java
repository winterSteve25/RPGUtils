package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
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

    public static class Deserializer implements IDeserializer<IReward> {
        @Override
        public IReward fromJson(JsonObject jsonObject) {
            return new ItemReward(ShapedRecipe.itemFromJson(jsonObject.getAsJsonObject("item")));
        }
    }
}
