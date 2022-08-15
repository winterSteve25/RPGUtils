package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public class InteractBlockTrigger {
    private final Block block;
    @Nullable
    private final TileEntity tileEntity;
    private final PlayerEntity player;
    private final Hand hand;

    public InteractBlockTrigger(Block block, @Nullable TileEntity tileEntity, PlayerEntity player, Hand hand) {
        this.block = block;
        this.tileEntity = tileEntity;
        this.player = player;
        this.hand = hand;
    }

    public Block getBlock() {
        return block;
    }

    @Nullable
    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }
}
