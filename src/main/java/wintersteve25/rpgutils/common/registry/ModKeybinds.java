package wintersteve25.rpgutils.common.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import wintersteve25.rpgutils.client.keybinds.Keybind;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;

public class ModKeybinds {
    public static final Keybind SKIP_DIALOGUE;

    static {
        SKIP_DIALOGUE = new Keybind(
                new KeyBinding("rpgutils.keys.skip_dialogue", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, "rpgutils.keys.category.npc"),
                DialogueUI.INSTANCE::skip
        );
    }

    public static void register() {
        SKIP_DIALOGUE.register();
    }
}
