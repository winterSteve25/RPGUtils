package wintersteve25.rpgutils.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.NearbyEntityOption;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.*;

public class DynamicUUIDUI extends BaseUI {

    private static final TranslationTextComponent NPC_ID_HINT = RLHelper.dialogueCreatorComponent("dynamic_uuid.npc_id");
    private static final TranslationTextComponent EMPTY_NPC_ID = RLHelper.dialogueCreatorComponent("dynamic_uuid.empty_id");
    private static final TranslationTextComponent SELECT_ENTITY = RLHelper.dialogueEditorComponent("select_entity");

    private DynamicUUID uuid;
    private Button selectEntity;
    private TextFieldWidget npcID;
    private Button confirm;

    private final Consumer<DynamicUUID> onSubmit;
    
    public DynamicUUIDUI(Consumer<DynamicUUID> onSubmit) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, 75);
        this.onSubmit = onSubmit;
    }

    @Override
    protected void init() {
        super.init();
        
        selectEntity = new Button(this.x + (WIDTH - 60) / 2, this.y + 35, 60, 20, SELECT_ENTITY, btn -> {
            Minecraft.getInstance().setScreen(new SelectNearbyEntity(false, selected -> {
                Minecraft.getInstance().setScreen(this);
                NearbyEntityOption option = selected.get(0);
                uuid.setUuid(option.getRepresents());
                selectEntity.setMessage(option.getTextComponent());
            }));
        });
        addButton(selectEntity);

        String value = npcID == null ? "" : npcID.getValue();
        npcID = new TextFieldWidget(Minecraft.getInstance() .font, this.x + (WIDTH - 150) / 2, this.y + 35, 150, 20, NPC_ID_HINT);
        npcID.setMaxLength(50);
        npcID.setVisible(true);
        npcID.setTextColor(16777215);
        npcID.setValue(value);
        addButton(npcID);

        BaseUI thisUI = this;
        confirm = new Button(this.x + (WIDTH - 60) / 2, this.y + 35, 60, 20, CONFIRM_TEXT, btn -> {
            if (uuid.getType() == DynamicUUID.DynamicType.DYNAMIC && npcID.getValue().isEmpty()) {
                Minecraft.getInstance().setScreen(new TextPrompt(EMPTY_NPC_ID) {
                    @Override
                    protected void Ok() {
                        Minecraft.getInstance().setScreen(thisUI);
                    }
                });
                return;
            }
            onSubmit.accept(create());
        });
        addButton(confirm);

        Dropdown<EnumDropdownOption<DynamicUUID.DynamicType>> dropdown = new Dropdown<>(this.x + (WIDTH - 60) / 2, this.y + 10, 60, null, EnumDropdownOption.populate(DynamicUUID.DynamicType.class));
        dropdown.setOnChanged(this::select);
        dropdown.select(0);
        addButton(dropdown);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        UIUtilities.textFieldHint(pMatrixStack, NPC_ID_HINT, npcID);
    }

    private DynamicUUID create() {
        if (uuid.getType() == DynamicUUID.DynamicType.DYNAMIC) {
            uuid.setDynamicNpcID(npcID.getValue());
        }
        return uuid;
    }

    private void select(EnumDropdownOption<DynamicUUID.DynamicType> selected) {
        uuid = new DynamicUUID(selected.getValue());

        switch (uuid.getType()) {
            case FIXED:
                selectEntity.visible = true;
                npcID.visible = false;
                confirm.y = this.y + 60;
                break;
            case DYNAMIC:
                selectEntity.visible = false;
                npcID.visible = true;
                confirm.y = this.y + 60;
                break;
            default:
                selectEntity.visible = false;
                npcID.visible = false;
                confirm.y = this.y + 35;
                break;
        }
    }
}
