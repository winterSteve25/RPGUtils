package wintersteve25.rpgutils.common.systems;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueRenderer;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueManager;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialoguePoolManager;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketPlayDialogue;
import wintersteve25.rpgutils.common.utils.RandomCollection;

import java.util.List;

public class DialogueSystem {
    
    private static boolean isPlaying = false;
    
    @OnlyIn(Dist.CLIENT)
    public static void play(PlayerEntity player, String dialoguePoolId) {
        RPGUtils.LOGGER.info("Attempting to play dialogue pool: {}", dialoguePoolId);
        
        List<DialogueRule> pool = DialoguePoolManager.INSTANCE.getPools().get(dialoguePoolId);
        
        if (pool == null) {
            RPGUtils.LOGGER.error("Given pool name is not found");
            return;
        }

        if (pool.isEmpty()) {
            RPGUtils.LOGGER.error("No dialogue found in pool");
            return;
        }
        
        RandomCollection<DialogueRule> rules = new RandomCollection<>();
        boolean any = false;
        
        for (DialogueRule rule : pool) {
            if (rule.isValid()) {
                rules.add(rule.getWeight(), rule);
                any = true;
            }
        }
        
        if (!any) {
            RPGUtils.LOGGER.warn("No dialogues are available for pool: {}", dialoguePoolId);
            return;
        }
        
        DialogueRule randomRule = rules.next();
        String dialogueId = randomRule.getDialogueId(Minecraft.getInstance().getLanguageManager().getSelected().getJavaLocale());
        Dialogue dialogue = DialogueManager.INSTANCE.getDialogues().get(dialogueId); 
        
        if (dialogue == null) {
            RPGUtils.LOGGER.error("Specified dialogue: {} was not found", dialogueId);
            return;
        }

        setDialogue(dialogue, player);
    }

    public static void stopDialogue() {
        if (isPlaying) setDialogue(null, null);
    }
    
    private static void setDialogue(Dialogue dialogue, PlayerEntity player) {
        if (dialogue == null) {
            Minecraft.getInstance().setScreen(null);
            return;
        }

        isPlaying = true;
        Minecraft.getInstance().setScreen(new DialogueRenderer(new DialogueUI(dialogue, player)));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void play(String dialoguePool) {
        play(Minecraft.getInstance().player, dialoguePool);
    }
}
