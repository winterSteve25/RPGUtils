package wintersteve25.rpgutils.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.security.auth.NTNumericCredential;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.components.prompt.TextPrompt;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.NearbyEntityOption;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.client.ui.selections.npc_id.SelectNpcID;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.*;

public class DynamicUUIDUI extends BaseUI {

    private static final TranslationTextComponent NPC_ID_HINT = RLHelper.dialogueCreatorComponent("dynamic_uuid.npc_id");
    private static final TranslationTextComponent EMPTY_NPC_ID = RLHelper.dialogueCreatorComponent("dynamic_uuid.empty_id");
    private static final TranslationTextComponent EMPTY_UUID = RLHelper.dialogueCreatorComponent("dynamic_uuid.empty_uuid");
    private static final TranslationTextComponent SELECT_ENTITY = RLHelper.dialogueEditorComponent("select_entity");

    private DynamicUUID uuid;
    private Button selectEntity;
    private Button npcID;
    private Button confirm;
    private Dropdown<EnumDropdownOption<DynamicUUID.DynamicType>> dropdown;

    private String speakerNpcID = "";
    private DynamicUUID initialData;
    
    private final Consumer<DynamicUUID> onSubmit;
    
    public DynamicUUIDUI(Consumer<DynamicUUID> onSubmit, DynamicUUID initialData) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, 75);
        this.onSubmit = onSubmit;
        this.initialData = initialData;
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

        npcID = new Button(this.x + (WIDTH - 150) / 2, this.y + 35, 150, 20, npcID == null ? NPC_ID_HINT : npcID.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new SelectNpcID(false, selected -> {
                speakerNpcID = selected.get(0).getNpcID();
                npcID.setMessage(new StringTextComponent(speakerNpcID));
                Minecraft.getInstance().setScreen(this);
            }));
        });
        addButton(npcID);

        BaseUI thisUI = this;
        confirm = new Button(this.x + (WIDTH - 60) / 2, this.y + 35, 60, 20, CONFIRM_TEXT, btn -> {
            if (uuid.getType() == DynamicUUID.DynamicType.DYNAMIC && speakerNpcID.isEmpty()) {
                minecraft.setScreen(new TextPrompt(EMPTY_NPC_ID) {
                    @Override
                    protected void Ok() {
                        minecraft.setScreen(thisUI);
                    }
                });
                return;
            } else if (uuid.getType() == DynamicUUID.DynamicType.FIXED && (uuid == null || uuid.getUuid() == null)) {
                minecraft.setScreen(new TextPrompt(EMPTY_UUID) {
                    @Override
                    protected void Ok() {
                        minecraft.setScreen(thisUI);
                    }
                });
                return;
            }
            
            onSubmit.accept(create());
        });
        addButton(confirm);

        Dropdown<EnumDropdownOption<DynamicUUID.DynamicType>> dropdownNew = new Dropdown<>(this.x + (WIDTH - 60) / 2, this.y + 10, 60, null, EnumDropdownOption.populate(DynamicUUID.DynamicType.class));
        dropdownNew.setOnChanged(this::select);
        if (dropdown == null) {
            dropdownNew.select(0);
        } else {
            dropdownNew.select(dropdown.getSelectedIndex());
        }
        dropdown = dropdownNew;
        addButton(dropdown);
        
        if (initialData != null) {
            switch (initialData.getType()) {
                case FIXED:
                    dropdown.select(0);
                    uuid.setUuid(initialData.getUuid());
                    selectEntity.setMessage(getEntityName(uuid.getUuid()));
                    break;
                case PLAYER:
                    dropdown.select(1);
                    break;
                case DYNAMIC:
                    dropdown.select(2);
                    speakerNpcID = initialData.getDynamicNpcID();
                    npcID.setMessage(new StringTextComponent(speakerNpcID));
                    break;
            }
            initialData = null;
        }
    }
    
    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }
    
    private DynamicUUID create() {
        if (uuid.getType() == DynamicUUID.DynamicType.DYNAMIC) {
            uuid.setDynamicNpcID(speakerNpcID);
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
    
    private ITextComponent getEntityName(UUID uuid) {
        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos posStart = player.blockPosition().offset(-24, -24, -24);
        BlockPos posEnd = player.blockPosition().offset(24, 24, 24);

        Optional<Entity> entity = player.getCommandSenderWorld().getEntities(player, new AxisAlignedBB(posStart, posEnd)).stream().filter(e -> e.getUUID().equals(uuid)).findFirst();
        return entity.map(Entity::getName).orElse(StringTextComponent.EMPTY);
    }
}
