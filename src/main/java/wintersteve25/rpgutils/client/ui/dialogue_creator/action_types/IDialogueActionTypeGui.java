package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;

public interface IDialogueActionTypeGui {
    void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget);

    void remove(BaseUI parent);
    
    void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks);
    
    default void tick() {
    }
    
    IDialogueAction save();
}
