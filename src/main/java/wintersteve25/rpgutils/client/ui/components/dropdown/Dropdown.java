package wintersteve25.rpgutils.client.ui.components.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class Dropdown extends Widget {
    
    private final List<IDropdownOption> options;
    private boolean focused;
    private int selectedIndex;
    
    public Dropdown(int x, int y, int width, ITextComponent text, List<IDropdownOption> options) {
        super(x, y, width, 20, text);
        this.options = options;
        this.focused = false;
        this.selectedIndex = -1;
    }

    @Override
    public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        pMatrixStack.pushPose();

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        minecraft.getTextureManager().bind(WIDGETS_LOCATION);
        int i = this.getYImage(this.isHovered());
        this.blit(pMatrixStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, 20);
        this.blit(pMatrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, 20);
        int j = getFGColor();

        if (selectedIndex == -1) {
            drawString(pMatrixStack, fontrenderer, this.getMessage(), this.x + this.width / 16, this.y + (20 - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        } else if (selectedIndex >= 0 && selectedIndex < options.size()) {
            options.get(selectedIndex).render(pMatrixStack, this.x + this.width / 16, this.y + (20 - 8) / 2, pMouseX, pMouseY);
        }
        
        if (focused) {
            int y = this.y + 20;
            
            for (IDropdownOption option : options) {
                minecraft.getTextureManager().bind(WIDGETS_LOCATION);
                pMatrixStack.pushPose();
                pMatrixStack.translate(0, 0, 200);
                
                int i2 = 1;
                
                if (pMouseX > this.x && pMouseX < this.x + this.width && pMouseY > y && pMouseY < y + 20) {
                    i2 = 2;
                }
                
                this.blit(pMatrixStack, this.x, y, 0, 46 + i2 * 20, this.width / 2, 20);
                this.blit(pMatrixStack, this.x + this.width / 2, y, 200 - this.width / 2, 46 + i2 * 20, this.width / 2, 20);
                
                option.render(pMatrixStack, this.x + this.width / 16, y + (20 - 8) / 2, pMouseX, pMouseY);
                pMatrixStack.popPose();
                y += 20;
            }
        }

        pMatrixStack.popPose();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        focused = !focused;
        
        if (focused) {
            setHeight((options.size() + 1) * 20);
        } else {
            int relativePos = (int) (mouseY - this.y);
            int option = relativePos / 20;
            if (option == 0) return;
            select(option - 1);
            
            setHeight(20);
        }
    }
    
    public void select(int index) {
        selectedIndex = index;
    }
}
