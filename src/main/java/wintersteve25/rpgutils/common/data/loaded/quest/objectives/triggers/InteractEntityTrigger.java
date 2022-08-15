package wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class InteractEntityTrigger {
    private final Entity interacted;
    private final ActionResultType interactResult;
    private final PlayerEntity player;
    private final Hand hand;
    
    public InteractEntityTrigger(Entity interacted, ActionResultType interactResult, PlayerEntity player, Hand hand) {
        this.interacted = interacted;
        this.interactResult = interactResult;
        this.player = player;
        this.hand = hand;
    }

    public Entity getInteracted() {
        return interacted;
    }

    public ActionResultType getInteractResult() {
        return interactResult;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }
}
