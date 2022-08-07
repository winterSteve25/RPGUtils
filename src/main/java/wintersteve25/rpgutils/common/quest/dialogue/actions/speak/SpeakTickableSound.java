package wintersteve25.rpgutils.common.quest.dialogue.actions.speak;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SpeakTickableSound extends TickableSound {
    private final Entity speaker;
    
    protected SpeakTickableSound(SoundEvent event, SoundCategory category, Entity speaker) {
        super(event, category);
        this.speaker = speaker;

        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
        
        this.x = (float)speaker.getX();
        this.y = (float)speaker.getY();
        this.z = (float)speaker.getZ();
    }

    public void forceStop() {
        stop();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return speaker.isAlive();
    }

    @Override
    public void tick() {
        if (!this.speaker.isAlive()) {
            this.stop();
        }
    }
}
