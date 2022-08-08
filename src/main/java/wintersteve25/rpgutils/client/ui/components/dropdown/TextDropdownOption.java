package wintersteve25.rpgutils.client.ui.components.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class TextDropdownOption implements IDropdownOption {
    
    private final ITextComponent text;

    public TextDropdownOption(ITextComponent text) {
        this.text = text;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY) {
        AbstractGui.drawCenteredString(matrixStack, Minecraft.getInstance().font, text, x, y, TextFormatting.WHITE.getColor());
    }
}
