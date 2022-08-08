package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;

public class DialogueCreatorUI extends BaseUI {

    private static final ResourceLocation BG = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/dialogue_editor.png");

    private static final int WIDTH = 400;
    private static final int HEIGHT = 240;
    
    protected DialogueCreatorUI() {
        super(BG, WIDTH, HEIGHT);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        this.renderBackgroundTexture(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
