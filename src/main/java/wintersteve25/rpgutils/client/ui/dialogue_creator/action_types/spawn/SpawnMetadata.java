package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.spawn;

import net.minecraft.util.math.BlockPos;

public class SpawnMetadata {
    private final BlockPos pos;
    private final String NpcID;

    public SpawnMetadata(BlockPos pos, String npcID) {
        this.pos = pos;
        NpcID = npcID;
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getNpcID() {
        return NpcID;
    }
}
