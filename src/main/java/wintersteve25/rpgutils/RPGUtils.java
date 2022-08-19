package wintersteve25.rpgutils;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wintersteve25.rpgutils.common.events.ServerForgeEvents;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.registry.ModCapabilities;
import wintersteve25.rpgutils.common.registry.ModEntities;
import wintersteve25.rpgutils.common.registry.ModMemoryModuleTypes;
import wintersteve25.rpgutils.common.utils.DataUtils;

@Mod(RPGUtils.MOD_ID)
public class RPGUtils {
    public static final String MOD_ID = "rpgutils";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    public RPGUtils() {
        DataUtils.register();
        ModNetworking.registerMessages();
        
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.register(modBus);
        ModMemoryModuleTypes.register(modBus);
        modBus.addListener(RPGUtils::commonSetup);
        
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, ServerForgeEvents::attachCapabilities);
    }
    
    private static void commonSetup(FMLCommonSetupEvent event) {
        ModCapabilities.register();
    }
}