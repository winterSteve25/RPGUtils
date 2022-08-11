package wintersteve25.rpgutils.client.ui.selections.nearby_entities;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;

import java.util.UUID;

public class NearbyEntityOption extends SelectionOption<NearbyEntityOption> {
    private final ITextComponent textComponent;
    private final UUID represents;

    public NearbyEntityOption(int x, int y, Entity entity, AbstractSelectionUI<NearbyEntityOption> parent, int index) {
        super(x, y, I18n.get(entity.getName().getString()), parent, index);
        this.textComponent = entity.getName();
        this.represents = entity.getUUID();
    }
    
    public NearbyEntityOption(NearbyEntityOption copyFrom) {
        super(copyFrom);
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