package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Consumer;

public interface IObjective {
    boolean isCompleted(PlayerEntity player);

    JsonElement toJson();

    ITextComponent objectiveTitle();
    
    void openEditObjectiveMenu(Consumer<IObjective> newObjective, Runnable onComplete);
}