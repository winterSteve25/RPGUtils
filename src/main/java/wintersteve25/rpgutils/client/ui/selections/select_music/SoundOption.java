package wintersteve25.rpgutils.client.ui.selections.select_music;

import net.minecraft.util.SoundEvent;
import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;

public class SoundOption extends SelectionOption<SoundOption> {
    private final SoundEvent soundEvent;
    
    public SoundOption(int x, int y, SelectSound parent, int index, SoundEvent soundEvent) {
        super(x, y, soundEvent == null ? "none" : soundEvent.getLocation().toString(), parent, index);
        this.soundEvent = soundEvent;
    }
    
    public SoundOption(SoundOption copyFrom) {
        super(copyFrom);
        this.soundEvent = copyFrom.soundEvent;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}