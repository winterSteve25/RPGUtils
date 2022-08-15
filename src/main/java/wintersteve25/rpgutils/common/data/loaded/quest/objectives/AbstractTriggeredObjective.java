package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import net.minecraft.entity.player.PlayerEntity;

public abstract class AbstractTriggeredObjective implements IObjective {
    
    private boolean triggered;
    
    public abstract void trigger(Object trigger);
    
    @Override
    public boolean isCompleted(PlayerEntity player) {
        if (triggered) {
            triggered = false;
            return true;
        }
        return false;
    }
    
    protected void triggered() {
        triggered = true;
    }
}
