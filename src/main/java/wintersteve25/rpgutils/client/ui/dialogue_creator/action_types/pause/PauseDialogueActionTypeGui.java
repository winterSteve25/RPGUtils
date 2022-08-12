package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.pause;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.math.NumberUtils;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.IDialogueActionTypeGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.PauseAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

public class PauseDialogueActionTypeGui implements IDialogueActionTypeGui {
    private static final TranslationTextComponent HINT = RLHelper.dialogueEditorComponent("pause_duration");
    private static final TranslationTextComponent SKIPPABLE = RLHelper.dialogueEditorComponent("skippable");
    
    private TextFieldWidget input;
    private ToggleButton skippableToggle;

    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget widgetParent) {
        String value = input == null ? "1" : input.getValue();
        input = new TextFieldWidget(Minecraft.getInstance().font, x + 180, y + 5, 150, 20, HINT);
        input.setMaxLength(50);
        input.setVisible(true);
        input.setTextColor(16777215);
        input.setValue(value);
        input.setFilter(NumberUtils::isCreatable);
        parent.addButton(input);

        boolean initialState = skippableToggle != null && skippableToggle.isStateTriggered();
        skippableToggle = new ToggleButton(x + 350, y + 8, 12, 12, initialState, btn -> btn.setStateTriggered(!btn.isStateTriggered()));
        skippableToggle.initTextureValues(7, 208, 15, 15, ModConstants.Resources.BLANK_SCREEN);
        parent.addButton(skippableToggle);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        UIUtilities.tooltipWhenOver(matrixStack, skippableToggle, mouseX, mouseY, Lists.newArrayList(SKIPPABLE));
        if (input == null) return;
        UIUtilities.textFieldHint(matrixStack, HINT, input);
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(input);
        parent.removeButton(skippableToggle);
    }

    @Override
    public void tick() {
        input.tick();
    }

    @Override
    public IDialogueAction save() {
        return new PauseAction(Float.parseFloat(input.getValue()), skippableToggle.isStateTriggered());
    }

    @Override
    public void load(Object[] data) {
        input.setValue(String.valueOf((float) data[0]));
        skippableToggle.setStateTriggered((boolean) data[1]);
    }
}
