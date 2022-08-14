package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.DialoguePredicateConfigUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.DialoguePredicateType;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.RLHelper;

public abstract class NestedPredicateGui implements IAttachedUI<DialoguePredicate> {

    private static final TranslationTextComponent CONDITION1 = RLHelper.dialogueEditorComponent("predicates.nested_condition", 1);
    private static final TranslationTextComponent CONDITION2 = RLHelper.dialogueEditorComponent("predicates.nested_condition", 2);
    
    private Button condition1Menu;
    private Button condition2Menu;
    
    private DialoguePredicate condition1;
    private DialoguePredicate condition2;

    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
        x = x + (parent.getTexWidth() - 120) / 2;
        
        condition1Menu = new Button(x, y + 35, 120, 20, condition1Menu == null ? CONDITION1 : condition1Menu.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new DialoguePredicateConfigUI((predicate, type) -> {
                Minecraft.getInstance().setScreen(parent);
                condition1 = predicate;
                condition1Menu.setMessage(new StringTextComponent(type.toString()));
            }, condition1));
        });
        parent.addButton(condition1Menu);
        condition2Menu = new Button(x, y + 60, 120, 20, condition2Menu == null ? CONDITION2 : condition2Menu.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new DialoguePredicateConfigUI((predicate, type) -> {
                Minecraft.getInstance().setScreen(parent);
                condition2 = predicate;
                condition2Menu.setMessage(new StringTextComponent(type.toString()));
            }, condition2));
        });
        parent.addButton(condition2Menu);
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(condition1Menu);
        parent.removeButton(condition2Menu);
    }

    @Override
    public DialoguePredicate save() {
        return condition1 == null || condition2 == null ? null : save(condition1, condition2);
    }

    @Override
    public void load(Object[] data) {
        condition1 = (DialoguePredicate) data[0];
        condition2 = (DialoguePredicate) data[1];

        if (condition1 != null) {
            condition1Menu.setMessage(new StringTextComponent(DialoguePredicateType.values()[condition1.guiIndex()].toString()));
        }

        if (condition2 != null) {
            condition2Menu.setMessage(new StringTextComponent(DialoguePredicateType.values()[condition2.guiIndex()].toString()));
        }
    }

    protected abstract DialoguePredicate save(DialoguePredicate condition1, DialoguePredicate condition2);
}
