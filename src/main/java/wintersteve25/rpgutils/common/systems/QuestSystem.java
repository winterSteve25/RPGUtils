package wintersteve25.rpgutils.common.systems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.HashSet;

public class QuestSystem {
    public static void attemptStartQuest(PlayerEntity player, ResourceLocation resourceLocation) {
        player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
            Quest quest = QuestsManager.INSTANCE.getQuests().get(resourceLocation);
            
            if (quest == null) {
                RPGUtils.LOGGER.error("Attempted to start quest {}, but it does not exist", resourceLocation);
                return;
            }
            
            if (!new HashSet<>(cap.getCompleted()).containsAll(quest.getPrerequisite())) {
                RPGUtils.LOGGER.info("Attempted to start quest {}, but player does not have the needed prerequisite(s)", resourceLocation);
                return;
            }
            
            cap.setCurrentActiveQuest(quest);
        });
    }
}