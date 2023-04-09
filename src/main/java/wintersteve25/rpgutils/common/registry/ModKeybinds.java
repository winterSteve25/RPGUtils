package wintersteve25.rpgutils.common.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import wintersteve25.rpgutils.client.keybinds.Keybind;
import wintersteve25.rpgutils.client.ui.quests.player.QuestUI;

public class ModKeybinds {
    public static final Keybind OPEN_QUESTS;

    static {
        OPEN_QUESTS = new Keybind(create("open_quests", GLFW.GLFW_KEY_H, KeyConflictContext.IN_GAME), QuestUI::open);
    }

    public static void register() {
        OPEN_QUESTS.register();
    }
    
    private static KeyBinding create(String name, int key, KeyConflictContext keyConflictContext) {
        return new KeyBinding("rpgutils.keys." + name, keyConflictContext, InputMappings.Type.KEYSYM, key, "rpgutils.keys.category");
    }
}
