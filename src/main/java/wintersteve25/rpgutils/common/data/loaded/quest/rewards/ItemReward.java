package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.crafting.CraftingHelper;
import wintersteve25.rpgutils.common.utils.IDeserializer;

public class ItemReward implements IReward {
    
    private final ItemStack item;

    public ItemReward(ItemStack item) {
        this.item = item;
    }

    @Override
    public void giveReward(ServerPlayerEntity player) {
        ItemStack itemStack = item.copy();
        
        if (player.addItem(itemStack)) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity itementity = player.drop(itemStack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setOwner(player.getUUID());
            }
        }
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", "item");
        jsonObject.add("item", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, item).result().get());
        
        return jsonObject;
    }

    @Override
    public Icon rewardIcon() {
        return ItemIcon.getItemIcon(item);
    }

    @Override
    public ITextComponent rewardTitle() {
        return item.getDisplayName().plainCopy().append(" x" + item.getCount());
    }

    @Override
    public boolean isValidReward() {
        return !item.isEmpty();
    }

    public ItemStack getItem() {
        return item;
    }

    public static class Deserializer implements IDeserializer<IReward> {
        @Override
        public IReward fromJson(JsonObject jsonObject) {
            return new ItemReward(ItemStack.CODEC.parse(JsonOps.INSTANCE, jsonObject.getAsJsonObject("item")).result().get());
        }
    }
}
