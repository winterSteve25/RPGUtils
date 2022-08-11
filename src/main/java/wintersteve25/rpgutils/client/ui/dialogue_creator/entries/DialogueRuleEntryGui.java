package wintersteve25.rpgutils.client.ui.dialogue_creator.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.math.NumberUtils;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;
import wintersteve25.rpgutils.client.ui.components.list.AbstractListEntryWidget;
import wintersteve25.rpgutils.client.ui.selections.dialogue_registry.DialogueOption;
import wintersteve25.rpgutils.client.ui.selections.dialogue_registry.SelectDialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

public class DialogueRuleEntryGui extends AbstractListEntryWidget {

    private static final TranslationTextComponent SELECT_SOUND = RLHelper.dialogueCreatorComponent("select_dialogue");
    private static final TranslationTextComponent DIALOGUE_WEIGHT = RLHelper.dialogueCreatorComponent("dialogue_weight");
    private static final TranslationTextComponent INTERRUPTABLE = RLHelper.dialogueCreatorComponent("interruptable");
    
    private Button selectDialogue;
    private Dialogue selectedDialogue;
    private TextFieldWidget dialogueWeight;
    private ToggleButton interruptable;
    
    public DialogueRuleEntryGui(int index) {
        super(225, 25, StringTextComponent.EMPTY, index);
    }

    @Override
    public void init(int parentX, int parentY, BaseUI parent) {
        super.init(parentX, parentY, parent);        
        
        selectDialogue = new Button(this.x + 25, this.y + 5, 100, 20, selectDialogue == null ? SELECT_SOUND : selectDialogue.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new SelectDialogue(false, selected -> {
                Minecraft.getInstance().setScreen(parent);
                DialogueOption option = selected.get(0);
                selectedDialogue = option.getDialogue();
                selectDialogue.setMessage(new StringTextComponent(selectedDialogue.getResourceLocation().toString()));
            }));
        });
        parent.addButton(selectDialogue);

        String value = dialogueWeight == null ? "1" : dialogueWeight.getValue();
        dialogueWeight = new TextFieldWidget(Minecraft.getInstance().font, x + 145, y + 5, 150, 20, DIALOGUE_WEIGHT);
        dialogueWeight.setMaxLength(50);
        dialogueWeight.setVisible(true);
        dialogueWeight.setTextColor(16777215);
        dialogueWeight.setFilter(NumberUtils::isCreatable);
        dialogueWeight.setValue(value);
        parent.addButton(dialogueWeight);
        
        boolean initialState = interruptable != null && interruptable.isStateTriggered();
        interruptable = new ToggleButton(this.x + 310, this.y + 8, 12, 12, initialState, btn -> btn.setStateTriggered(!btn.isStateTriggered()));
        interruptable.initTextureValues(7, 208, 15, 15, ModConstants.Resources.BLANK_SCREEN);
        parent.addButton(interruptable);
        parent.addButton(this);
    }

    @Override
    public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        UIUtilities.tooltipWhenOver(pMatrixStack, dialogueWeight, pMouseX, pMouseY, Lists.newArrayList(DIALOGUE_WEIGHT));
        UIUtilities.tooltipWhenOver(pMatrixStack, interruptable, pMouseX, pMouseY, Lists.newArrayList(INTERRUPTABLE));
    }

    @Override
    public void remove(BaseUI parent) {
        super.remove(parent);
        parent.removeButton(selectDialogue);
        parent.removeButton(dialogueWeight);
        parent.removeButton(interruptable);
        parent.removeButton(this);
    }
    
    public DialogueRule create() {
        if (selectedDialogue == null) {
            return null;
        }
        
        return new DialogueRule(Float.parseFloat(dialogueWeight.getValue()), interruptable.isStateTriggered(), selectedDialogue.getResourceLocation());
    }
}
