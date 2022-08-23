package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;

public class RenameQuestPrompt extends TextInputPrompt {
    
    public QuestBuilderButton button;
    
    public RenameQuestPrompt(QuestCreatorUI panel, ITextComponent title, String hintText, BiConsumer<TextInputPrompt, MouseButton> onConfirmed, BiConsumer<TextInputPrompt, MouseButton> onDenied) {
        super(panel, title, hintText, onConfirmed, onDenied);
    }

    public void enable(QuestBuilderButton button) {
        this.button = button;
        enable();
    }
}
