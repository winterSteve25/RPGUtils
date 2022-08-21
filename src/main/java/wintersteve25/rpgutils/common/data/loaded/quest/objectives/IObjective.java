package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.PlayerEntity;

public interface IObjective {
    boolean isCompleted(PlayerEntity player);

    JsonElement toJson();
}
