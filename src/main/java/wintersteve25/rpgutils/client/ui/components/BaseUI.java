package wintersteve25.rpgutils.client.ui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public abstract class BaseUI extends Screen {
    
    protected final ResourceLocation bg;
    protected final int texWidth;
    protected final int texHeight;
    
    protected int x;
    protected int y;
    
    protected BaseUI(ResourceLocation bg, int texWidth, int texHeight) {
        super(StringTextComponent.EMPTY);
        this.bg = bg;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    @Override
    protected void init() {
        this.x = (this.width - texWidth) / 2;
        this.y = (this.height - texHeight) / 2;
    }

    @Override
    public <T extends Widget> T addButton(T p_230480_1_) {
        return super.addButton(p_230480_1_);
    }

    @Override
    public <T extends IGuiEventListener> T addWidget(T p_230481_1_) {
        return super.addWidget(p_230481_1_);
    }

    public <T extends Widget> void removeButton(T btn) {
        buttons.remove(btn);
        children.remove(btn);
    }

    public <T extends IGuiEventListener> void removeWidget(T widget) {
        children.remove(widget);
    }

    protected void renderBackgroundTexture(MatrixStack matrixStack) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(bg);
        this.blit(matrixStack, this.x, this.y, 0, 0, texWidth, texHeight);
    }

    public int getTexWidth() {
        return texWidth;
    }

    public int getTexHeight() {
        return texHeight;
    }
}
