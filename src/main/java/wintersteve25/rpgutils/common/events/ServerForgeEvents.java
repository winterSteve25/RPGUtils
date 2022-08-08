package wintersteve25.rpgutils.common.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.quest.data.QuestsManager;
import wintersteve25.rpgutils.common.quest.dialogue.DialogueManager;
import wintersteve25.rpgutils.common.quest.dialogue_pool.DialoguePoolManager;
import wintersteve25.rpgutils.common.registry.ModCommands;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerForgeEvents {
    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(QuestsManager.INSTANCE);
        event.addListener(DialogueManager.INSTANCE);
        event.addListener(DialoguePoolManager.INSTANCE);
    }
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher());
    }
}