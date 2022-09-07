package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import dev.ftb.mods.ftblibrary.ui.IOpenableScreen;
import net.minecraft.util.text.ITextComponent;
import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.function.Consumer;

public interface IObjectiveType<T extends IObjective> extends IDeserializer<T> {
    IOpenableScreen configScreen(Consumer<T> onSubmit);
    
    ITextComponent name();
}
