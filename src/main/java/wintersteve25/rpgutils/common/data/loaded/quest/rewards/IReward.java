package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IReward {
    /**
     * @param player The player that should receive the reward
     * This method will be called when a reward is claimed. It should give the player an appropriate reward. 
     */
    void giveReward(ServerPlayerEntity player);
    
    JsonElement toJson();

    ITextComponent rewardTitle();

    boolean isValidReward();
}