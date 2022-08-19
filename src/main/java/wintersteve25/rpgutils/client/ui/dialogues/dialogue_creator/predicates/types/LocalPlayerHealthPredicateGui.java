package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.math.NumberUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.LocalPlayerHealthPredicate;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class LocalPlayerHealthPredicateGui implements IAttachedUI<DialoguePredicate> {
    
    private static final TranslationTextComponent OPERAND = RLHelper.dialogueEditorComponent("predicates.local_player_health.operand");
    
    private Dropdown<EnumDropdownOption<LocalPlayerHealthPredicate.HealthPredicateType>> dropdown;
    private TextFieldWidget operandField;
    
    private Object[] initialData;

    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
        x = x + (parent.getTexWidth() - 120) / 2;
        
        String prevValue = operandField == null ? "20" : operandField.getValue();
        operandField = new TextFieldWidget(Minecraft.getInstance().font, x, y + 60, 120, 20, OPERAND);
        operandField.setMaxLength(50);
        operandField.setVisible(true);
        operandField.setTextColor(16777215);
        operandField.setValue(prevValue);
        operandField.setFilter(NumberUtils::isCreatable);
        parent.addButton(operandField);
        
        Dropdown<EnumDropdownOption<LocalPlayerHealthPredicate.HealthPredicateType>> dropdownNew = new Dropdown<>(x, y + 35, 120, null, EnumDropdownOption.populate(LocalPlayerHealthPredicate.HealthPredicateType.class));
        if (dropdown == null) {
            dropdownNew.select(0);
        } else {
            dropdownNew.select(dropdown.getSelectedIndex());
        }
        dropdown = dropdownNew;
        parent.addButton(dropdown);
    
        if (initialData != null) {
            LocalPlayerHealthPredicate.HealthPredicateType type = (LocalPlayerHealthPredicate.HealthPredicateType) initialData[0];
            int index = Arrays.asList(LocalPlayerHealthPredicate.HealthPredicateType.values()).indexOf(type);
            dropdown.select(index);
            operandField.setValue(String.valueOf(initialData[1]));
            initialData = null;
        }
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(operandField);
        parent.removeButton(dropdown);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        UIUtilities.textFieldHint(matrixStack, OPERAND, operandField);
    }

    @Override
    public DialoguePredicate save() {
        return new LocalPlayerHealthPredicate(dropdown.getSelected().getValue(), Float.parseFloat(operandField.getValue()));
    }

    @Override
    public void load(Object[] data) {
        initialData = data;
    }
}
