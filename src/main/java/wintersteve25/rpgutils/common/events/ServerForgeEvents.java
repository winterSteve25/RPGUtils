package wintersteve25.rpgutils.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.registry.ModCommands;
import wintersteve25.rpgutils.common.registry.ModEntities;

import java.util.Set;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerForgeEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher());
    }
    
    @SubscribeEvent
    public static void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ServerOnlyLoadedData.reloadAll();
    }

    @SubscribeEvent
    public static void playerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.getCommandSenderWorld().isClientSide()) {
            NpcIDMapping.refreshClient((ServerPlayerEntity) player);
        }
        World world = player.level;
        if (world instanceof ServerWorld) {
            for (Entity entity : ((ServerWorld) world).getEntities(ModEntities.NPC_ENTITY.get(), (e) -> true)) {
                if (entity instanceof NPCEntity) {
                    ((NPCEntity) entity).updateClient((ServerPlayerEntity) player);
                }
            }
        }
    }
    
//    @SubscribeEvent
//    public static void onPlayerInteract(PlayerInteractEvent.EntityInteractSpecific event) {
//        PlayerEntity player = event.getPlayer();
//        if (!player.getCommandSenderWorld().isClientSide()) return;
//        if (player instanceof FakePlayer) return;
//        Entity entity = event.getTarget();
//        UUID targetUUID = entity.getUUID();
//
//        DialoguePoolManager.INSTANCE.getPools().values().stream().filter(dialogueRules -> !dialogueRules.isEmpty() && dialogueRules.get(0).);
//    }
}