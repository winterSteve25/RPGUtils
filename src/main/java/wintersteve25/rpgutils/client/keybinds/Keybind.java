package wintersteve25.rpgutils.client.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybind {
    private final KeyBinding keybind;
    private final Runnable onPressed; 
    
    public Keybind(KeyBinding keybind, Runnable onPressed) {
        this.keybind = keybind;
        this.onPressed = onPressed;
    }
    
    public void press() {
        onPressed.run();
    }

    public KeyBinding getKeybind() {
        return keybind;
    }

    public void register() {
        KeybindEvent.KEYBINDS.add(this);
        ClientRegistry.registerKeyBinding(keybind);
    }
}
