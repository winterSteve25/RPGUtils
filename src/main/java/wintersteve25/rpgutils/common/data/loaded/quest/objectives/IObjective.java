package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.IOpenableScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Consumer;

public interface IObjective {
    boolean isCompleted(PlayerEntity player);

    JsonElement toJson();

    Icon objectiveIcon();
    
    ITextComponent objectiveTitle();
    
    void openEditObjectiveMenu(Consumer<IObjective> newObjective, Runnable onComplete);
}