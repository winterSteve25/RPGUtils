package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;

public class NoPredicateGui implements IAttachedUI<DialoguePredicate> {
    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
    }

    @Override
    public void remove(BaseUI parent) {
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
