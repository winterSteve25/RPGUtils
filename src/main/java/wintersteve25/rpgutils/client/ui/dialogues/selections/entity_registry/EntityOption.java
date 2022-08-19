package wintersteve25.rpgutils.client.ui.dialogues.selections.entity_registry;

import net.minecraft.entity.EntityType;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;

public class EntityOption extends SelectionOption<EntityOption> {
    
    private final EntityType<?> type;
    
    public EntityOption(int x, int y, EntityType<?> type, AbstractSelectionUI<EntityOption> parent, int index) {
        super(x, y, type.getRegistryName().toString(), parent, index);
        this.type = type;
    }

    public EntityOption(EntityOption copyFrom) {
        super(copyFrom);
        this.type = copyFrom.type;
    }

    public EntityType<?> getType() {
        return type;
    }
}
