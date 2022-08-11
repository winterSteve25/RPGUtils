package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.prompt.ConfirmationUI;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.function.Consumer;

public class EnterTextConfirmationUI extends ConfirmationUI {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("create_dialogue");
    private static final TranslationTextComponent NAME_HINT = RLHelper.dialogueCreatorComponent("create_dialogue_name");
    private static final TranslationTextComponent EMPTY = RLHelper.dialogueCreatorComponent("empty_text");
    
    private final Consumer<String> onCreate;
    private final Runnable onCancel;
    private TextFieldWidget nameInput;
    
    public EnterTextConfirmationUI(Consumer<String> onCreate, Runnable onCancel) {
        super(TITLE, 200, 80);
        this.onCreate = onCreate;
        this.onCancel = onCancel;
    }

    @Override
    protected void init() {
        super.init();

        String value = nameInput == null ? "" : nameInput.getValue();
        nameInput = new TextFieldWidget(Minecraft.getInstance().font, this.x + (texWidth - 155) / 2, this.y + 30, 155, 20, NAME_HINT);
        nameInput.setMaxLength(50);
        nameInput.setVisible(true);
        nameInput.setTextColor(16777215);
        nameInput.setValue(value);
        addButton(nameInput);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        UIUtilities.textFieldHint(pMatrixStack, NAME_HINT, nameInput);
    }

    @Override
    protected void onAccept(Button button) {
        if (nameInput.getValue().isEmpty()) {
            BaseUI thisUI = this;
            
            Minecraft.getInstance().setScreen(new TextPrompt(EMPTY) {
                @Override
                protected void Ok() {
                    Minecraft.getInstance().setScreen(thisUI);
                }
            });
            return;
        }
        onCreate.accept(nameInput.getValue());
    }

    @Override
    protected void onDecline(Button button) {
        onCancel.run();
    }
}
