package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.action_types.spawn;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.math.NumberUtils;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.dialogues.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.client.ui.dialogues.selections.npc_id.SelectNpcID;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.dialogues.components.selection.AbstractSelectionUI.WIDTH;

public class SpawnMetadataUI extends BaseUI {

    private static final TranslationTextComponent X_FIELD = RLHelper.dialogueEditorComponent("spawn.pos_x");
    private static final TranslationTextComponent Y_FIELD = RLHelper.dialogueEditorComponent("spawn.pos_y");
    private static final TranslationTextComponent Z_FIELD = RLHelper.dialogueEditorComponent("spawn.pos_z");
    private static final TranslationTextComponent NPC_ID = RLHelper.dialogueEditorComponent("spawn.npc_id");
    private static final TranslationTextComponent CONFIRM = RLHelper.dialogueEditorComponent("spawn.confirm");

    private final Consumer<SpawnMetadata> onSubmit;

    private SpawnMetadata initialData;

    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private TextFieldWidget zField;
    private Button npcID;
    private String npcIDChosen;
    
    protected SpawnMetadataUI(Consumer<SpawnMetadata> onSubmit, SpawnMetadata initialData) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, SelectNearbyEntity.HEIGHT);
        this.onSubmit = onSubmit;
        this.initialData = initialData;
    }

    @Override
    protected void init() {
        super.init();

        RayTraceResult context = Minecraft.getInstance().hitResult;
        BlockPos pos;
                
        if (context != null) {
            Vector3d vector3d = context.getLocation();
            pos = new BlockPos(vector3d.x(), vector3d.y(), vector3d.z());
        } else {
            pos = Minecraft.getInstance().player.blockPosition();
        }
        
        xField = getField(xField, this.x + 15, this.y + 20, 40, X_FIELD, String.valueOf(pos.getX()));
        yField = getField(yField, this.x + 70, this.y + 20, 40, Y_FIELD, String.valueOf(pos.getY()));
        zField = getField(zField, this.x + 125, this.y + 20, 40, Z_FIELD, String.valueOf(pos.getZ()));
        
        npcID = new Button(this.x + 15, y + 55, 150, 20, npcID == null ? NPC_ID : npcID.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new SelectNpcID(false, selected -> {
                npcIDChosen = selected.get(0).getNpcID();
                npcID.setMessage(new StringTextComponent(npcIDChosen));
                Minecraft.getInstance().setScreen(this);
            }));
        });
        addButton(npcID);
        
        Button submit = new Button(this.x + (WIDTH - 100) / 2, this.y + 140, 100, 20, CONFIRM, btn -> {
            onSubmit.accept(new SpawnMetadata(new BlockPos(
                    Integer.parseInt(xField.getValue()),
                    Integer.parseInt(yField.getValue()),
                    Integer.parseInt(zField.getValue())
            ), npcIDChosen));
        });
        addButton(submit);
        
        if (initialData != null) {
            BlockPos pos1 = initialData.getPos();
            xField.setValue(String.valueOf(pos1.getX()));
            yField.setValue(String.valueOf(pos1.getY()));
            zField.setValue(String.valueOf(pos1.getZ()));
            npcID.setMessage(new StringTextComponent(initialData.getNpcID()));
            initialData = null;
        }
    }
    
    private TextFieldWidget getField(TextFieldWidget fieldWidget, int x, int y, int width, ITextComponent text, String defaultValue) {
        String prevValue = fieldWidget == null ? defaultValue : fieldWidget.getValue();
        TextFieldWidget textField = new TextFieldWidget(Minecraft.getInstance().font, x, y, width, 20, text);
        textField.setMaxLength(50);
        textField.setVisible(true);
        textField.setTextColor(16777215);
        textField.setValue(prevValue);
        textField.setFilter(NumberUtils::isCreatable);
        addButton(textField);
        return textField;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        renderBackgroundTexture(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        UIUtilities.textFieldHint(matrixStack, X_FIELD, xField);
        UIUtilities.textFieldHint(matrixStack, Y_FIELD, yField);
        UIUtilities.textFieldHint(matrixStack, Z_FIELD, zField);
        
        UIUtilities.tooltipWhenOver(matrixStack, xField, mouseX, mouseY, Lists.newArrayList(X_FIELD));
        UIUtilities.tooltipWhenOver(matrixStack, yField, mouseX, mouseY, Lists.newArrayList(Y_FIELD));
        UIUtilities.tooltipWhenOver(matrixStack, zField, mouseX, mouseY, Lists.newArrayList(Z_FIELD));
        UIUtilities.tooltipWhenOver(matrixStack, npcID, mouseX, mouseY, Lists.newArrayList(NPC_ID));
    }
}
