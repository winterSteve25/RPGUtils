package wintersteve25.rpgutils.client.ui.dialogues;

import net.minecraft.entity.player.PlayerEntity;

public class DialogueContext {
    public final PlayerEntity player;

    public DialogueContext(PlayerEntity player) {
        this.player = player;
    }
}