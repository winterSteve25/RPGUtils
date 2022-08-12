package wintersteve25.rpgutils.client.ui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public class UIUtilities {
    public static void tooltipWhenOver(MatrixStack matrixStack, Widget widget, int mouseX, int mouseY, List<ITextComponent> texts) {
        if (mouseX >= widget.x && mouseX < widget.x + widget.getWidth() && mouseY >= widget.y && mouseY < widget.y + widget.getHeight()) {
            Minecraft minecraft = Minecraft.getInstance();
            MainWindow window = minecraft.getWindow();
            GuiUtils.drawHoveringText(matrixStack, texts, mouseX, mouseY, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 999, minecraft.font);
        }
    }
    
    public static void textFieldHint(MatrixStack pMatrixStack, ITextComponent hint, TextFieldWidget textField) {
        if (textField.visible && !textField.isFocused() && textField.getValue().isEmpty()) {
            AbstractGui.drawString(pMatrixStack, Minecraft.getInstance().font, hint, textField.x + 5, textField.y + 6, TextFormatting.GRAY.getColor());
        }
    }
}
