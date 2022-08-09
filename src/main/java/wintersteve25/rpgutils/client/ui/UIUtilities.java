package wintersteve25.rpgutils.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public class UIUtilities {
    public static void tooltipWhenOver(MatrixStack matrixStack, Widget widget, int mouseX, int mouseY, List<ITextComponent> texts) {
        if (mouseX > widget.x && mouseX < widget.x + widget.getWidth() && mouseY > widget.y && mouseY < widget.y + widget.getHeight()) {
            Minecraft minecraft = Minecraft.getInstance();
            MainWindow window = minecraft.getWindow();
            GuiUtils.drawHoveringText(matrixStack, texts, mouseX, mouseY, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 999, minecraft.font);
        }
    }
}
