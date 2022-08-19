package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator;

import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.entries.DialogueRuleEntryGui;
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
    private final List<DialogueRule> initialData;
    
    public DialoguePoolUI(Consumer<List<DialogueRule>> onSubmit, List<DialogueRule> initialData) {
        super(TITLE);
        this.onSubmit = onSubmit;
        this.initialData = initialData;
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
            minecraft.setScreen(new TextPrompt(NO_DIALOGUE) {
                @Override
                protected void Ok() {
                    minecraft.setScreen(DialoguePoolUI.this);
                }
            });

            return;
        }
        
        onSubmit.accept(rules);
    }

    @Override
    protected void populateEntries() {
        if (initialData == null || initialData.isEmpty()) return;
        for (DialogueRule rule : initialData) {
            DialogueRuleEntryGui entryGui = createEntry();
            entryGui.load(rule);
            addEntry(entryGui);
        }
    }
}
