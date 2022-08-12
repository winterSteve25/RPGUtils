package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak;

import net.minecraft.util.SoundEvent;

public class SpeakMetadata {
    private final SoundEvent audio;
    private final int initialTypeSpeed;
    private final boolean waitForInput;

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
}
