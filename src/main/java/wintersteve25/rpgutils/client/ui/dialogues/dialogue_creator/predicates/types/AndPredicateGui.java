package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;

public class AndPredicateGui implements IAttachedUI<DialoguePredicate> {

    private Button condition1Menu;
    private Button condition2Menu;
    
    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
        condition1Menu = new Button(x + 5, y + 5, 100, 20, new StringTextComponent("Placeholder"), btn -> {});
        parent.addButton(condition1Menu);
        condition2Menu = new Button(x + 5, y + 40, 100, 20, new StringTextComponent("Placeholder 2"), btn -> {});
        parent.addButton(condition2Menu);
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(condition1Menu);
        parent.removeButton(condition2Menu);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public DialoguePredicate save() {
        return null;
    }

    @Override
    public void load(Object[] data) {
        
    }
}
