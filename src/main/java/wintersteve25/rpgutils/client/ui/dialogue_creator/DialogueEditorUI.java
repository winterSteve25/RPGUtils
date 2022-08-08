package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialogueActionEntryGui;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.List;
import java.util.UUID;

public class DialogueEditorUI extends EditableListUI<DialogueActionEntryGui> {

    private static final ResourceLocation BG = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/dialogue_editor.png");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 240;

    protected DialogueEditorUI() {
        super(BG, WIDTH, HEIGHT);
    }

    @Override
    protected DialogueActionEntryGui createEntry(int index) {
        return new DialogueActionEntryGui(index);
    }

    @Override
    protected void save(List<DialogueActionEntryGui> data) {
        JsonArray lines = new JsonArray();

        for (DialogueActionEntryGui actionGui : data) {
            JsonObject line = new JsonObject();
            UUID selectedEntity = actionGui.getSelectedEntity();
            line.addProperty("speaker", selectedEntity == null ? "PLAYER" : selectedEntity.toString());
            line.add("action", actionGui.getActionTypeGui().save().toJson());
            lines.add(line);
        }

        JsonUtilities.saveDialogue(lines);
    }
}