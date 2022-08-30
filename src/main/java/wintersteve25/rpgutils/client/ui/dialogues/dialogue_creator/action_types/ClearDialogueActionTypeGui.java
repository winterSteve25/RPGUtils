package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.action_types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.ClearAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;

public class ClearDialogueActionTypeGui implements IAttachedUI<IDialogueAction> {
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
    public IDialogueAction save() {
        return new ClearAction();
    }

    @Override
    public void load(Object[] data) {
    }
}
