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
        OPEN_QUESTS = new Keybind(
                new KeyBinding("rpgutils.keys.open_quests", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_H, "rpgutils.keys.category.npc"),
                QuestUI::open
        );
    }

    public static void register() {
        OPEN_QUESTS.register();
    }
}
