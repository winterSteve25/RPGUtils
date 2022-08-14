package wintersteve25.rpgutils.client.ui.dialogues.selections.entity_registry;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectEntity extends AbstractSelectionUI<EntityOption> {
    public SelectEntity(boolean allowMultiple, Consumer<List<EntityOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void populateOptions(List<EntityOption> list) {
        List<EntityType<?>> entities = new ArrayList<>(ForgeRegistries.ENTITIES.getValues());

        for (int i = 0; i < entities.size(); i++) {
            list.add(new EntityOption(this.x + 10, this.y + 40 + i * 12, entities.get(i), this, i));
        }
    }

    @Override
    protected EntityOption copyFrom(EntityOption copyFrom) {
        return new EntityOption(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof EntityOption;
    }
}
