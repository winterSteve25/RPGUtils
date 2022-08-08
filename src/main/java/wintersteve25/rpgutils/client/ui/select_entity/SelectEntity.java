package wintersteve25.rpgutils.client.ui.select_entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;

import java.util.List;
import java.util.function.Consumer;

public class SelectEntity extends AbstractSelectionUI<EntityOption> {
    public SelectEntity(boolean allowMultiple, Consumer<List<EntityOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void populateOptions(List<EntityOption> list) {
        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos posStart = player.blockPosition().offset(-16, -16, -16);
        BlockPos posEnd = player.blockPosition().offset(16, 16, 16);

        List<Entity> entitiesInRange = player.getCommandSenderWorld().getEntities(player, new AxisAlignedBB(posStart, posEnd));

        for (int i = 0; i < entitiesInRange.size(); i++) {
            list.add(new EntityOption(this.x + 10, this.y + 40 + i * 12, entitiesInRange.get(i), this, i));
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