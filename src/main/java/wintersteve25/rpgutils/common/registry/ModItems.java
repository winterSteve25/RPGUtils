package wintersteve25.rpgutils.common.registry;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.items.DialogueCreator;

public class ModItems {
    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, RPGUtils.MOD_ID);

    public static RegistryObject<Item> DIALOGUE_CREATOR = REGISTER.register("dialogue_creator", DialogueCreator::new);
    
    public static void register(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}
