package wintersteve25.rpgutils.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.dropdown.Dropdown;
import wintersteve25.rpgutils.client.ui.components.dropdown.EnumDropdownOption;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.NearbyEntityOption;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.utils.ModConstants;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.*;

public class DynamicUUIDUI extends BaseUI {

    private DynamicUUID uuid;
    private Button selectEntity;
    private Button confirm;
    
    public DynamicUUIDUI() {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        Dropdown<EnumDropdownOption<DynamicUUID.DynamicType>> dropdown = new Dropdown<>(this.x + (WIDTH - 60) / 2, this.y + 10, 60, null, EnumDropdownOption.populate(DynamicUUID.DynamicType.class));
        dropdown.select(0);
        dropdown.setOnChanged(this::select);
        addButton(dropdown);
        
        // TODO: remove placeholder
        selectEntity = new Button(this.x + (WIDTH - 60) / 2, this.y + 35, 60, 20, new StringTextComponent("Placeholder"), btn -> {
            Minecraft.getInstance().setScreen(new SelectNearbyEntity(false, selected -> {
                Minecraft.getInstance().setScreen(this);
                NearbyEntityOption option = selected.get(0);
                uuid.setUuid(option.getRepresents());
                selectEntity.setMessage(option.getTextComponent());
            }));
        });
        
        confirm = new Button(this.x + (WIDTH - 60) / 2, this.y + 35, 60, 20, CONFIRM_TEXT, btn -> {
            
        });
        addButton(confirm);
    }
    
    private void select(EnumDropdownOption<DynamicUUID.DynamicType> selected) {
        uuid = new DynamicUUID(selected.getValue());
        
        if (uuid.getType() == DynamicUUID.DynamicType.FIXED) {
            confirm.y = this.y + 60;
        } else {
            confirm.y = this.y + 35;
        }
    }
}
