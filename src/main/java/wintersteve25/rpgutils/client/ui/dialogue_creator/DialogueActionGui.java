package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;
import wintersteve25.rpgutils.client.ui.select_entity.EntityOption;
import wintersteve25.rpgutils.client.ui.select_entity.SelectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DialogueActionGui extends Widget {
    private static final TranslationTextComponent DIALOGUE_HINT = new TranslationTextComponent("rpgutils.gui.dialogue_creator.line_input");
    private static final TranslationTextComponent SELECT_ENTITY = new TranslationTextComponent("rpgutils.gui.dialogue_creator.select_entity");
    private static final TranslationTextComponent UUID = new TranslationTextComponent("rpgutils.gui.dialogue_creator.selected_uuid");

    private int index;
    
    private UUID selectedEntity;
    private ITextComponent selectedName;

    private Button selectEntity;
    private TextFieldWidget input;
    private ToggleButton toggleButton;
    
    
    
    public DialogueActionGui(int index) {
        super(0, 0, 225, 25, StringTextComponent.EMPTY);
        this.index = index;
    }
    
    public void init(int parentX, int parentY, BaseUI parent) {
        remove(parent);
        
        this.x = parentX + 5;
        this.y = parentY + 35 + 30 * index;
        
        selectEntity = new Button(this.x + 25, this.y + 5, 60, 20, selectedName == null ? SELECT_ENTITY : selectedName, btn -> {
            Minecraft.getInstance().setScreen(new SelectEntity(false, selected -> {
                Minecraft.getInstance().setScreen(parent);
                EntityOption option = selected.get(0);
                selectedEntity = option.getRepresents();
                selectedName = option.getTextComponent();
                selectEntity.setMessage(selectedName);
            }));
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
        parent.addButton(selectEntity);
        
        String value = input == null ? "" : input.getValue();
        input = new TextFieldWidget(Minecraft.getInstance().font, this.x + 100, this.y + 5, 150, 20, DIALOGUE_HINT);
        input.setMaxLength(50);
        input.setVisible(true);
        input.setTextColor(16777215);
        input.setValue(value);
        parent.addButton(input);
        
        boolean initialState = toggleButton != null && toggleButton.isStateTriggered();
        toggleButton = new ToggleButton(this.x, this.y + 8, 12, 12, initialState, btn -> btn.setStateTriggered(!btn.isStateTriggered()));
        toggleButton.initTextureValues(7, 208, 15, 15, SelectEntity.BG);
        parent.addButton(toggleButton);
        parent.addButton(this);
    }
    
    public void tick() {
        input.tick();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!input.isFocused() && input.getValue().isEmpty()) {
            drawString(matrixStack, Minecraft.getInstance().font, DIALOGUE_HINT, input.x + 5, input.y + 6, TextFormatting.GRAY.getColor());
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected() {
        return toggleButton.isStateTriggered();
    }

    public void remove(BaseUI parent) {
        parent.removeButton(selectEntity);
        parent.removeButton(input);
        parent.removeButton(toggleButton);
        parent.removeButton(this);
    }
}