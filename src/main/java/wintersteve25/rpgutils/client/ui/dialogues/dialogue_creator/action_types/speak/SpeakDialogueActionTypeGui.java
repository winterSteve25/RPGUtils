package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.action_types.speak;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.speak.SpeakAction;
import wintersteve25.rpgutils.common.utils.RLHelper;

public class SpeakDialogueActionTypeGui implements IAttachedUI<IDialogueAction> {
    private static final TranslationTextComponent DIALOGUE_HINT = RLHelper.dialogueEditorComponent("line_input");

    private TextFieldWidget input;
    private Button metadata;
    
    private SpeakMetadata speakMetadata;
    private Object[] initialData;
    
    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
        String value = input == null ? "" : input.getValue();
        input = new TextFieldWidget(Minecraft.getInstance().font, x + 180, y + 5, 150, 20, DIALOGUE_HINT);
        input.setMaxLength(50);
        input.setVisible(true);
        input.setTextColor(16777215);
        input.setValue(value);
        parent.addButton(input);

        metadata = new Button(x + 350, y + 5, 20, 20, new StringTextComponent("M"), btn -> {
            SpeakMetadataUI ui = new SpeakMetadataUI(metadata -> {
                speakMetadata = metadata;
                Minecraft.getInstance().setScreen(parent);
            }, speakMetadata);
            Minecraft.getInstance().setScreen(ui);
        });
        parent.addButton(metadata);
        
        if (initialData != null) {
            if (speakMetadata == null) speakMetadata = new SpeakMetadata(null, 0, false);
            speakMetadata.setAudio((SoundEvent) initialData[0]);
            input.setValue((String) initialData[1]);
            speakMetadata.setInitialTypeSpeed((int) initialData[2]);
            speakMetadata.setWaitForInput((boolean) initialData[3]);
            initialData = null;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (input == null) return;
        UIUtilities.textFieldHint(matrixStack, DIALOGUE_HINT, input);
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(input);
        parent.removeButton(metadata);
    }

    @Override
    public void tick() {
        input.tick();
    }

    @Override
    public IDialogueAction save() {
        if (speakMetadata == null) speakMetadata = new SpeakMetadata(null, 10, true);
        return new SpeakAction(input.getValue(), speakMetadata.getAudio(), speakMetadata.getInitialTypeSpeed(), speakMetadata.isWaitForInput());
    }

    @Override
    public void load(Object[] data) {
        initialData = data;
    }
}
