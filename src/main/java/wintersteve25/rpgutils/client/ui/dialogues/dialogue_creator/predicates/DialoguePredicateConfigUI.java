package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.client.ui.dialogues.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.CONFIRM_TEXT;
import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.WIDTH;

public class DialoguePredicateConfigUI extends BaseUI {

    private static final TranslationTextComponent EMPTY_PREDICATE = RLHelper.dialogueEditorComponent("predicates.empty");

    private final BiConsumer<DialoguePredicate, DialoguePredicateType> onSubmit;

    private Dropdown<EnumDropdownOption<DialoguePredicateType>> dropdown;
    private IAttachedUI<DialoguePredicate> predicateAttachedUI;
    
    private DialoguePredicate initialData;
    
    public DialoguePredicateConfigUI(BiConsumer<DialoguePredicate, DialoguePredicateType> onSubmit, DialoguePredicate initialData) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, SelectNearbyEntity.HEIGHT);
        this.onSubmit = onSubmit;
        this.initialData = initialData;
    }

    @Override
    protected void init() {
        super.init();
        
        Button save = new Button(this.x + (WIDTH - 70) / 2, this.y + 145, 70, 20, CONFIRM_TEXT, btn -> {
            if (predicateAttachedUI == null) {
                noPredicate();
                return;
            }
            
            DialoguePredicate predicate = predicateAttachedUI.save();
            DialoguePredicateType type = dropdown.getSelected().getValue();
            
            if (type != DialoguePredicateType.NONE && predicate == null) {
                noPredicate();
                return;
            }

            onSubmit.accept(predicate, type);
        });
        addButton(save);

        Dropdown<EnumDropdownOption<DialoguePredicateType>> dropdownNew = new Dropdown<>(this.x + (WIDTH - 120) / 2, this.y + 10, 120, null, EnumDropdownOption.populate(DialoguePredicateType.class));
        if (dropdown == null) {
            dropdownNew.select(0);
        } else {
            dropdownNew.select(dropdown.getSelectedIndex());
        }
        dropdownNew.setOnChanged(this::select);
        dropdown = dropdownNew;
        
        if (predicateAttachedUI == null) predicateAttachedUI = dropdown.getSelected().getValue().getGuiCreator().get();
        predicateAttachedUI.remove(this);
        predicateAttachedUI.init(x, y, x, y, this, null);
        
        addButton(dropdown);
        
        if (initialData != null) {
            dropdown.select(initialData.guiIndex());
            
            if (predicateAttachedUI != null) {
                predicateAttachedUI.load(initialData.data());
            }
            
            initialData = null;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        renderBackgroundTexture(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (predicateAttachedUI != null) {
            predicateAttachedUI.render(matrixStack, x, y, mouseX, mouseY, partialTicks);
        }
    }

    private void select(EnumDropdownOption<DialoguePredicateType> selected) {
        if (predicateAttachedUI != null) {
            predicateAttachedUI.remove(this);
        }
        predicateAttachedUI = selected.getValue().getGuiCreator().get();
        removeButton(dropdown);
        predicateAttachedUI.remove(this);
        predicateAttachedUI.init(x, y, x, y, this, null);
        if (dropdown != null) {
            addButton(dropdown);
        }
    }
    
    private void noPredicate() {
        BaseUI thisUI = this;

        minecraft.setScreen(new TextPrompt(EMPTY_PREDICATE) {
            @Override
            protected void Ok() {
                minecraft.setScreen(thisUI);
            }
        });
    }
}