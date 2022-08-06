package wintersteve25.rpgutils.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

public class DialogueCreatorUI extends Screen {
    private final CompoundNBT creator;
    
    protected DialogueCreatorUI(CompoundNBT creator) {
        super(StringTextComponent.EMPTY);
        this.creator = creator;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        pMatrixStack.pushPose();
        
        pMatrixStack.popPose();
    }

    public static void open(CompoundNBT creator) {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(new DialogueCreatorUI(creator));
    }
}