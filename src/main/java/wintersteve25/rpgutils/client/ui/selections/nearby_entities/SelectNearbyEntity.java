package wintersteve25.rpgutils.client.ui.selections.nearby_entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;

import java.util.List;
import java.util.function.Consumer;

public class SelectNearbyEntity extends AbstractSelectionUI<NearbyEntityOption> {
    public SelectNearbyEntity(boolean allowMultiple, Consumer<List<NearbyEntityOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void populateOptions(List<NearbyEntityOption> list) {
        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos posStart = player.blockPosition().offset(-24, -24, -24);
        BlockPos posEnd = player.blockPosition().offset(24, 24, 24);

        List<Entity> entitiesInRange = player.getCommandSenderWorld().getEntities(player, new AxisAlignedBB(posStart, posEnd));

        for (int i = 0; i < entitiesInRange.size(); i++) {
            list.add(new NearbyEntityOption(this.x + 10, this.y + 40 + i * 12, entitiesInRange.get(i), this, i));
        }
    }

    @Override
    protected NearbyEntityOption copyFrom(NearbyEntityOption copyFrom) {
        return new NearbyEntityOption(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof NearbyEntityOption;
    }
}