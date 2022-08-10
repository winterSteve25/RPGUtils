package wintersteve25.rpgutils.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.registry.ModEntities;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerModEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.NPC_ENTITY.get(), NPCEntity.createAttribute().build());
    }
}
