package wintersteve25.rpgutils.client.ui.quests.creator;

import com.google.common.collect.ImmutableList;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.config.ui.ItemSearchMode;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SelectBlockScreen extends SelectItemStackScreen {
    private SelectBlockScreen(ItemStackConfig c, ConfigCallback cb) {
        super(c, cb);
    }
    
    public static void open(ItemStackConfig c, ConfigCallback cb) {
        List<ItemSearchMode> previous = new ArrayList<>(modes);
        // very janky way to do this, but everything is private
        // TODO: fix concurrent modification crash
        modes.clear();
        modes.add(BlockSearchMode.INSTANCE);
        modes.add(BlockInventorySearchMode.INSTANCE);
        new SelectBlockScreen(c, cb).openGui();
        modes.clear();
        modes.addAll(previous);
    }
    
    private static class BlockSearchMode implements ItemSearchMode {
        public static final BlockSearchMode INSTANCE = new BlockSearchMode();
        
        private List<ItemStack> blockCache = null; 
        
        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(Items.GRASS_BLOCK);
        }

        @Override
        public IFormattableTextComponent getDisplayName() {
            return new StringTextComponent("Blocks");
        }

        @Override
        public Collection<ItemStack> getAllItems() {
            
            if (blockCache == null) {
                blockCache = new ArrayList<>();
                
                for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                    blockCache.add(new ItemStack(block.asItem()));
                }
            }
            
            return blockCache;
        }
    }
    
    private static class BlockInventorySearchMode implements ItemSearchMode {

        public static final BlockInventorySearchMode INSTANCE = new BlockInventorySearchMode();
        
        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(Items.CHEST);
        }

        @Override
        public IFormattableTextComponent getDisplayName() {
            return new StringTextComponent("Blocks in Inventory");
        }

        @Override
        public Collection<ItemStack> getAllItems() {
            PlayerEntity player = Minecraft.getInstance().player;

            if(player == null) {
                return Collections.emptySet();
            }

            int inv = player.inventory.getContainerSize();
            List<ItemStack> items = new ArrayList<>(inv);
            for (int i = 0; i < inv; i++) {
                ItemStack stack = Minecraft.getInstance().player.inventory.getItem(i);

                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof BlockItem) {
                        items.add(stack);
                    }
                }
            }
            
            return items;
        }
    }
}
