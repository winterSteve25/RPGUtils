package wintersteve25.rpgutils.client.ui.dialogue_creator;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialogueRuleEntryGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DialoguePoolUI extends EditableListUI<DialogueRuleEntryGui> {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("edit_pool");
    private static final TranslationTextComponent NO_DIALOGUE = RLHelper.dialogueCreatorComponent("no_dialogue");

    private final Consumer<List<DialogueRule>> onSubmit;
    
    public DialoguePoolUI(Consumer<List<DialogueRule>> onSubmit) {
        super(TITLE);
        this.onSubmit = onSubmit;
    }

    @Override
    protected DialogueRuleEntryGui createEntry(int index) {
        return new DialogueRuleEntryGui(index);
    }

    @Override
    protected void save(List<DialogueRuleEntryGui> data) {
        AtomicBoolean hasNull = new AtomicBoolean(false);
        
        List<DialogueRule> rules = data.stream().map((entry) -> {
            DialogueRule rule = entry.create();
            if (rule == null) hasNull.set(true);
            return rule;
        }).collect(Collectors.toList());
        
        if (hasNull.get()) {
            BaseUI thisUI = this;
            
            minecraft.setScreen(new TextPrompt(NO_DIALOGUE) {
                @Override
                protected void Ok() {
                    minecraft.setScreen(thisUI);
                }
            });

            return;
        }
        
        onSubmit.accept(rules);
    }
}
