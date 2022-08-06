package wintersteve25.rpgutils.common.quest.data.rewards;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import wintersteve25.rpgutils.common.utils.ISerializer;

public class ItemReward implements IReward {
    
    private final ItemStack item;

    public ItemReward(ItemStack item) {
        this.item = item;
    }

    @Override
    public void giveReward(PlayerEntity player) {
        player.addItem(item);
    }

    public static class Serializer implements ISerializer<IReward> {
        @Override
        public IReward fromJson(JsonObject jsonObject) {
            return new ItemReward(ShapedRecipe.itemFromJson(jsonObject.getAsJsonObject("item")));
        }
    }
}
