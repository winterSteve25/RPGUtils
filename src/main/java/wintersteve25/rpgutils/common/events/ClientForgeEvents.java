package wintersteve25.rpgutils.common.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.temporary.EntityIDMapper;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    
    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide()) {
            EntityIDMapper.CLIENT.data.put(event.getEntity().getUUID(), event.getEntity().getId());
        }
    }
}
