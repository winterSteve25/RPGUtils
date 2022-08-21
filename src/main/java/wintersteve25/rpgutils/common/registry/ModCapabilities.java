package wintersteve25.rpgutils.common.registry;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.capabilities.base.CapabilityStorage;
import wintersteve25.rpgutils.common.data.loaded.quest.PlayerQuestProgress;

public class ModCapabilities {
    @CapabilityInject(PlayerQuestProgress.class)    
    public static Capability<PlayerQuestProgress> PLAYER_QUEST;
    
    public static void register() {
        RPGUtils.LOGGER.info("Registering capabilities");
        CapabilityManager.INSTANCE.register(PlayerQuestProgress.class, new CapabilityStorage<>(), () -> new PlayerQuestProgress());
    }
}
