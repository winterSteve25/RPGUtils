package wintersteve25.rpgutils.client.keybinds;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wintersteve25.rpgutils.RPGUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeybindEvent {
    public static final List<Keybind> KEYBINDS = new ArrayList<>(); 
    
    @SubscribeEvent
    public static void onKeyDown(InputEvent.KeyInputEvent event) {
        List<Keybind> matching = KEYBINDS.stream().filter(keybind -> keybind.getKeybind().matches(event.getKey(), event.getScanCode())).collect(Collectors.toList());
        if (matching.isEmpty()) return;
        matching.get(0).press();
    }
}