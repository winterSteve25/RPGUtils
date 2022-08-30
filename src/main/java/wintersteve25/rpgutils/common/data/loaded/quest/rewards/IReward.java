package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.PlayerEntity;

public interface IReward {
    /**
     * @param player The player that should receive the reward
     * This method will be called when a reward is claimed. It should give the player an appropriate reward. 
     */
    void giveReward(PlayerEntity player);

    JsonElement toJson();
}
