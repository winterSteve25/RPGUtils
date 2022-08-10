package wintersteve25.rpgutils.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialoguePoolManager;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.registry.ModCommands;

import java.util.UUID;

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