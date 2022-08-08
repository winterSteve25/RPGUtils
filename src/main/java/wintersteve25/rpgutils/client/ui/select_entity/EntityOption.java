package wintersteve25.rpgutils.client.ui.select_entity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;

import java.util.UUID;

public class EntityOption extends SelectionOption {
    private final ITextComponent textComponent;
    private final UUID represents;

    public EntityOption(int x, int y, Entity entity, SelectEntity parent, int index) {
        super(x, y, I18n.get(entity.getName().getString()), parent, index);
        
        this.textComponent = entity.getName();
        this.represents = entity.getUUID();
    }
    
    public EntityOption(EntityOption copyFrom) {
        super(copyFrom.x, copyFrom.y, copyFrom.getText(), copyFrom.getParent(), copyFrom.getIndex());
        
        this.textComponent = copyFrom.textComponent;
        this.represents = copyFrom.represents;
    }
    
    public ITextComponent getTextComponent() {
        return textComponent;
    }

    public UUID getRepresents() {
        return represents;
    }
}