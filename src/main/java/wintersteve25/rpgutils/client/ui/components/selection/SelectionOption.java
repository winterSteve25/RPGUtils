package wintersteve25.rpgutils.client.ui.components.selection;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.rpgutils.common.utils.ModConstants;

public class SelectionOption extends Widget {
    
    private final String text;
    private final AbstractSelectionUI parent;
    private final int index;

    private boolean selected = false;
    
    public SelectionOption(int x, int y, String text, AbstractSelectionUI parent, int index) {
        super(x, y, 155, 12, StringTextComponent.EMPTY);
        
        this.text = text;
        this.parent = parent;
        this.index = index;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbstractGui.drawString(matrixStack, Minecraft.getInstance().font, text, x + 2, y + 2, TextFormatting.WHITE.getColor());
        if (selected) {
            Minecraft.getInstance().getTextureManager().bind(ModConstants.Resources.BLANK_SCREEN);
            blit(matrixStack, x, y, 7, 186, 155, 12);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        parent.setSelectedIndex(index);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    protected AbstractSelectionUI getParent() {
        return parent;
    }

    protected boolean isSelected() {
        return selected;
    }
}