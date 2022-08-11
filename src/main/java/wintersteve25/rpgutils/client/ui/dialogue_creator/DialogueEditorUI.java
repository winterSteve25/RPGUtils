package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialogueActionEntryGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;
import java.util.UUID;

public class DialogueEditorUI extends EditableListUI<DialogueActionEntryGui> {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueEditorComponent("title");

    private final Runnable onSubmit;
    private final ResourceLocation id;
    
    public DialogueEditorUI(Runnable onSubmit, ResourceLocation id) {
        super(TITLE);
        this.onSubmit = onSubmit;
        this.id = id;
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
            DynamicUUID selectedEntity = actionGui.getSelectedEntity();
            line.add("speaker", selectedEntity.toJson());
            line.add("action", actionGui.getActionTypeGui().save().toJson());
            lines.add(line);
        }

        JsonUtilities.saveDialogue(id, lines);
        
        onSubmit.run();
    }
}