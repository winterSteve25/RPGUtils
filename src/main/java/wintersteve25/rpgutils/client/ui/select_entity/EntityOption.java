package wintersteve25.rpgutils.client.ui.select_entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.UUID;

public class EntityOption extends Widget {
    private final ITextComponent textComponent;
    private final String text;
    private final UUID represents;
    private final SelectEntity parent;
    private final int index;

    private boolean selected = false;
    
    public EntityOption(int x, int y, Entity entity, SelectEntity parent, int index) {
        super(x, y, 155, 12, StringTextComponent.EMPTY);
        
        this.textComponent = entity.getName();
        this.text = I18n.get(textComponent.getString());
        this.represents = entity.getUUID();
        this.parent = parent;
        this.index = index;
    }
    
    public EntityOption(EntityOption copyFrom) {
        super(copyFrom.x, copyFrom.y, 155, 12, StringTextComponent.EMPTY);
        
        this.textComponent = copyFrom.textComponent;
        this.text = copyFrom.text;
        this.represents = copyFrom.represents;
        this.parent = copyFrom.parent;
        this.index = copyFrom.index;
        this.selected = copyFrom.selected;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbstractGui.drawString(matrixStack, Minecraft.getInstance().font, text, x + 2, y + 2, TextFormatting.WHITE.getColor());
        if (selected) {
            Minecraft.getInstance().getTextureManager().bind(SelectEntity.BG);
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

    public ITextComponent getTextComponent() {
        return textComponent;
    }

    public UUID getRepresents() {
        return represents;
    }

    public int getIndex() {
        return index;
    }
}