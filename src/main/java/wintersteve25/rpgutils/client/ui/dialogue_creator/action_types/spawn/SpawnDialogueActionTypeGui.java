package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.spawn;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.IDialogueActionTypeGui;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak.SpeakMetadata;
import wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak.SpeakMetadataUI;
import wintersteve25.rpgutils.client.ui.selections.entity_registry.SelectEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.SpawnAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.speak.SpeakAction;
import wintersteve25.rpgutils.common.utils.RLHelper;

public class SpawnDialogueActionTypeGui implements IDialogueActionTypeGui {
    private static final TranslationTextComponent ENTITY_TO_SPAWN = RLHelper.dialogueEditorComponent("entity_to_spawn");
    private static final TranslationTextComponent NO_ENTITY_SELECTED = RLHelper.dialogueEditorComponent("no_entity_selected");

    private BaseUI parent;
    private Button selectEntity;
    private EntityType<?> entityType;
    
    @Override
    public void init(int parentX, int parentY, int x, int y, BaseUI parent, Widget parentWidget) {
        this.parent = parent;
        selectEntity = new Button(x + 180, y + 5, 60, 20, ENTITY_TO_SPAWN, btn -> {
            Minecraft.getInstance().setScreen(new SelectEntity(false, data -> {
                Minecraft.getInstance().setScreen(parent);
                entityType = data.get(0).getType();
            }));
        });
        parent.addButton(selectEntity);
    }

    @Override
    public void remove(BaseUI parent) {
        parent.removeButton(selectEntity);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public IDialogueAction save() {
        if (entityType == null) {
            Minecraft.getInstance().setScreen(new TextPrompt(NO_ENTITY_SELECTED) {
                @Override
                protected void Ok() {
                    Minecraft.getInstance().setScreen(parent);
                }
            });
            return null;
        }
        return new SpawnAction(entityType.getRegistryName(), BlockPos.ZERO, "test");
    }
}
