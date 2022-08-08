package wintersteve25.rpgutils.common.registry;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;

public class ModItems {
    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, RPGUtils.MOD_ID);
    
    public static void register(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}
