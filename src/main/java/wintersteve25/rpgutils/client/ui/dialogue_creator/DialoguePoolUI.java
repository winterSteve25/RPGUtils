package wintersteve25.rpgutils.client.ui.dialogue_creator;

import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialogueRuleEntryGui;
import wintersteve25.rpgutils.common.quest.dialogue_pool.DialogueRule;

import java.util.List;
import java.util.function.Consumer;

public class DialoguePoolUI extends EditableListUI<DialogueRuleEntryGui> {

    private static final ResourceLocation BG = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/dialogue_editor.png");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 240;

    private final Consumer<List<DialogueRule>> onSubmit;
    
    public DialoguePoolUI(Consumer<List<DialogueRule>> onSubmit) {
        super(BG, WIDTH, HEIGHT);
        this.onSubmit = onSubmit;
    }

    @Override
    protected DialogueRuleEntryGui createEntry(int index) {
        return new DialogueRuleEntryGui(index);
    }

    @Override
    protected void save(List<DialogueRuleEntryGui> data) {
    }
}
