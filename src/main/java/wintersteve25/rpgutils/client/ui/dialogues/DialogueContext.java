package wintersteve25.rpgutils.client.ui.dialogues;

import net.minecraft.entity.player.PlayerEntity;

public class DialogueContext {
    public final PlayerEntity player;
    public int typeInterval;

    public DialogueContext(PlayerEntity player, int typeInterval) {
        this.player = player;
        this.typeInterval = typeInterval;
    }
}