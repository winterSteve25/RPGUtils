package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import wintersteve25.rpgutils.client.ui.components.BaseUI;

public interface IAttachedUI<T> {
    void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget);

    void remove(BaseUI parent);
    
    void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks);
    
    default void tick() {
    }
    
    T save();
    
    void load(Object[] data);
}
