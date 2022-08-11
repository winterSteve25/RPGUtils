package wintersteve25.rpgutils.client.ui.dialogue_creator.entries;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import wintersteve25.rpgutils.client.ui.DynamicUUIDUI;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.components.list.AbstractListEntryWidget;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.DialogueActionType;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.IDialogueActionTypeGui;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.NearbyEntityOption;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DialogueActionEntryGui extends AbstractListEntryWidget {
    private static final TranslationTextComponent SELECT_ENTITY = RLHelper.dialogueEditorComponent("select_entity");
    private static final TranslationTextComponent UUID = RLHelper.dialogueEditorComponent("selected_uuid");
    
    // TODO: make sure this is not null and add in the ui that allow you to create this
    private DynamicUUID selectedEntity;
    private ITextComponent selectedName;

    private Button selectEntity;
    private Dropdown<EnumDropdownOption<DialogueActionType>> dropdown;
    private IDialogueActionTypeGui actionTypeGui;
    
    public DialogueActionEntryGui(int index) {
        super(225, 25, StringTextComponent.EMPTY, index);
    }

    @Override
    public void init(int parentX, int parentY, BaseUI parent) {
        super.init(parentX, parentY, parent);
        
        selectEntity = new Button(this.x + 25, this.y + 5, 60, 20, selectedName == null ? SELECT_ENTITY : selectedName, btn -> {
            Minecraft.getInstance().setScreen(new DynamicUUIDUI());
        }, (btn, matrix, x, y) -> {
            Minecraft minecraft = Minecraft.getInstance();
            MainWindow window = minecraft.getWindow();
            List<ITextComponent> lines = new ArrayList<>();
            if (selectedEntity == null) {
                lines.add(UUID);
            } else {
                lines.add(new StringTextComponent(selectedEntity.toString()));
            }
            GuiUtils.drawHoveringText(matrix, lines, x, y, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 999, minecraft.font);
        });
    
        Dropdown<EnumDropdownOption<DialogueActionType>> dropdownNew = new Dropdown<>(x + 100, y + 5, 60, StringTextComponent.EMPTY, EnumDropdownOption.populate(DialogueActionType.class));
        if (dropdown == null) {
            dropdownNew.select(0);
        } else {
            dropdownNew.select(dropdown.getSelectedIndex());
        }
        
        dropdown = dropdownNew;
        parent.addButton(dropdown);

        if (actionTypeGui == null) actionTypeGui = dropdownNew.getSelected().getValue().getGuiCreator().get();
        actionTypeGui.remove(parent);
        actionTypeGui.init(parentX, parentY, x, y, parent, this);
        parent.addButton(selectEntity);
        parent.addButton(this);
        
        dropdownNew.setOnChanged(action -> {
            if (actionTypeGui != null) {
                actionTypeGui.remove(parent);
            }
            actionTypeGui = action.getValue().getGuiCreator().get();
            parent.removeButton(this);
            parent.removeButton(selectEntity);
            actionTypeGui.remove(parent);
            actionTypeGui.init(parentX, parentY, x, y, parent, this);
            parent.addButton(selectEntity);
            parent.addButton(this);
        });
    }

    @Override
    public void tick() {
        actionTypeGui.tick();
    }

    @Override
    public void remove(BaseUI parent) {
        super.remove(parent);
        
        parent.removeButton(selectEntity);
        parent.removeButton(dropdown);

        if (actionTypeGui != null) {
            actionTypeGui.remove(parent);
        }

        parent.removeButton(this);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        actionTypeGui.render(matrixStack, x, y, mouseX, mouseY, partialTicks);
    }
    
    public DynamicUUID getSelectedEntity() {
        return selectedEntity;
    }

    public IDialogueActionTypeGui getActionTypeGui() {
        return actionTypeGui;
    }
}