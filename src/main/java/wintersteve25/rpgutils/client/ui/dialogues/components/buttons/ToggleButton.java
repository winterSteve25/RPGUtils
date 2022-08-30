package wintersteve25.rpgutils.client.ui.dialogues.components.buttons;

import net.minecraft.client.gui.widget.ToggleWidget;

import java.util.function.Consumer;

public class ToggleButton extends ToggleWidget {
    private final Consumer<ToggleButton> pressable;
    
    public ToggleButton(int pX, int pY, int pWidth, int pHeight, boolean pInitialState, Consumer<ToggleButton> pressable) {
        super(pX, pY, pWidth, pHeight, pInitialState);
        this.pressable = pressable;
    }

    @Override
    public void onClick(double p_230982_1_, double p_230982_3_) {
        pressable.accept(this);
    }
}
