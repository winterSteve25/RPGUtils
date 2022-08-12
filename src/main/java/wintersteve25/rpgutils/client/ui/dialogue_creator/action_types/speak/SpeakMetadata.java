package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak;

import net.minecraft.util.SoundEvent;

public class SpeakMetadata {
    private SoundEvent audio;
    private int initialTypeSpeed;
    private boolean waitForInput;

    public SpeakMetadata(SoundEvent audio, int initialTypeSpeed, boolean waitForInput) {
        this.audio = audio;
        this.initialTypeSpeed = initialTypeSpeed;
        this.waitForInput = waitForInput;
    }

    public SoundEvent getAudio() {
        return audio;
    }

    public int getInitialTypeSpeed() {
        return initialTypeSpeed;
    }

    public boolean isWaitForInput() {
        return waitForInput;
    }

    public void setAudio(SoundEvent audio) {
        this.audio = audio;
    }

    public void setInitialTypeSpeed(int initialTypeSpeed) {
        this.initialTypeSpeed = initialTypeSpeed;
    }

    public void setWaitForInput(boolean waitForInput) {
        this.waitForInput = waitForInput;
    }
}