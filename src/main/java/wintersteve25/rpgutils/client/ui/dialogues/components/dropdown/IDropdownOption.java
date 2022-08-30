package wintersteve25.rpgutils.client.ui.dialogues.components.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IDropdownOption {
    void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY);
}