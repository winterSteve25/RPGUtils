package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialogueActionEntryGui;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialoguePoolEntryGui;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.List;
import java.util.UUID;

public class DialogueCreatorUI extends EditableListUI<DialoguePoolEntryGui> {

    private static final ResourceLocation BG = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/dialogue_editor.png");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 240;

    protected DialogueCreatorUI() {
        super(BG, WIDTH, HEIGHT);
    }

    @Override
    protected DialoguePoolEntryGui createEntry(int index) {
        return new DialoguePoolEntryGui(index);
    }

    @Override
    protected void save(List<DialoguePoolEntryGui> data) {
    }

    public static void open() {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(new DialogueCreatorUI());
    }
}
