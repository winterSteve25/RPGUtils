package wintersteve25.rpgutils.client.ui.dialogue_creator.predicates;

import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialoguePredicateEntryGui;

import java.util.List;

public class DialoguePredicatesUI extends EditableListUI<DialoguePredicateEntryGui> {
    public DialoguePredicatesUI(TranslationTextComponent title) {
        super(title);
    }

    @Override
    protected DialoguePredicateEntryGui createEntry(int index) {
        return new DialoguePredicateEntryGui(index);
    }

    @Override
    protected void save(List<DialoguePredicateEntryGui> data) {
        
    }
}
