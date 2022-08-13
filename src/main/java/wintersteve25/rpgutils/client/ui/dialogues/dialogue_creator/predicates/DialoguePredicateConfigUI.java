package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.client.ui.dialogues.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.ModConstants;

import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.CONFIRM_TEXT;
import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.WIDTH;

public class DialoguePredicateConfigUI extends BaseUI {
    
    private final Consumer<DialoguePredicate> onSubmit;

    private Dropdown<EnumDropdownOption<DialoguePredicateType>> dropdown;
    private DialoguePredicateType selected;
    private IAttachedUI<DialoguePredicate> predicateAttachedUI;
    
    public DialoguePredicateConfigUI(Consumer<DialoguePredicate> onSubmit) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, SelectNearbyEntity.HEIGHT);
        this.onSubmit = onSubmit;
    }

    @Override
    protected void init() {
        super.init();
        
        Button save = new Button(this.x + (WIDTH - 70) / 2, this.y + 145, 70, 20, CONFIRM_TEXT, btn -> onSubmit.accept(null));
        addButton(save);

        Dropdown<EnumDropdownOption<DialoguePredicateType>> dropdownNew = new Dropdown<>(this.x + (WIDTH - 60) / 2, this.y + 10, 60, null, EnumDropdownOption.populate(DialoguePredicateType.class));
        dropdownNew.setOnChanged(this::select);
        if (dropdown == null) {
            dropdownNew.select(0);
        } else {
            dropdownNew.select(dropdown.getSelectedIndex());
        }
        dropdown = dropdownNew;
        
        if (predicateAttachedUI == null) predicateAttachedUI = selected.getGuiCreator().get();
        predicateAttachedUI.remove(this);
        predicateAttachedUI.init(x, y, x, y, this, null);
        
        addButton(dropdown);
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.renderBackground(matrixStack);
        renderBackgroundTexture(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    private void select(EnumDropdownOption<DialoguePredicateType> selected) {
        this.selected = selected.getValue();

        if (predicateAttachedUI != null) {
            predicateAttachedUI.remove(this);
        }
        predicateAttachedUI = this.selected.getGuiCreator().get();
        removeButton(dropdown);
        predicateAttachedUI.remove(this);
        predicateAttachedUI.init(x, y, x, y, this, null);
        addButton(dropdown);
    }
}