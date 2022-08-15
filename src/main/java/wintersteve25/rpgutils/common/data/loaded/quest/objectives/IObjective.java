package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import net.minecraft.entity.player.PlayerEntity;

public interface IObjective {
    boolean isCompleted(PlayerEntity player);
    
    
}
