package wintersteve25.rpgutils;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wintersteve25.rpgutils.common.network.ModNetworking;
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
    }
}