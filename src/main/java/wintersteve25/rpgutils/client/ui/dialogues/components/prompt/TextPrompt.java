package wintersteve25.rpgutils.client.ui.dialogues.components.prompt;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.common.utils.RLHelper;

public abstract class TextPrompt extends BaseUI {

    private static final TranslationTextComponent ACCEPT = RLHelper.uiComponent("prompt.ok");

    private final TranslationTextComponent title;

    protected TextPrompt(TranslationTextComponent title) {
        super(null, 200, 40);
        this.title = title;
    }

    @Override
    protected void init() {
        super.init();
        int i = this.x + (texWidth - 60) / 2;
        addButton(new Button(i, this.y + 15, 60, 20, ACCEPT, btn -> Ok()));
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);

        FontRenderer fontRenderer = minecraft.font;
        int titleWidth = fontRenderer.width(title);
        drawString(pMatrixStack, fontRenderer, title, this.x + (texWidth - titleWidth) / 2, this.y, TextFormatting.WHITE.getColor());

        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }
    
    protected abstract void Ok();
}
