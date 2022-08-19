package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.prompt.ConfirmationUI;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.entries.DialoguePoolEntryGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialoguePoolManager;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.data.loaded.storage.ClientOnlyLoadedData;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DialogueCreatorUI extends EditableListUI<DialoguePoolEntryGui> {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("title");
    private static final TranslationTextComponent EMPTY_POOL_NAME = RLHelper.dialogueCreatorComponent("empty_pool_name");

    private final List<ResourceLocation> toRemoves;
    
    protected DialogueCreatorUI() {
        super(TITLE);
        toRemoves = new ArrayList<>();
    }

    @Override
    protected DialoguePoolEntryGui createEntry(int index) {
        return new DialoguePoolEntryGui(index);
    }

    @Override
    protected void save(List<DialoguePoolEntryGui> data) {
        if (!data.stream().anyMatch(DialoguePoolEntryGui::isPoolnameEmpty)) {
            saveInternal(data);
            return;
        }
        Minecraft.getInstance().setScreen(new ConfirmationUI(EMPTY_POOL_NAME, 200, 60) {
            @Override
            protected void onAccept(Button button) {
                saveInternal(data);
            }

            @Override
            protected void onDecline(Button button) {
                Minecraft.getInstance().setScreen(DialogueCreatorUI.this);
            }
        });
    }
    
    private void saveInternal(List<DialoguePoolEntryGui> data) {
        for (DialoguePoolEntryGui poolEntry : data) {
            Tuple<ResourceLocation, List<DialogueRule>> pool = poolEntry.create();

            JsonArray dialogues = new JsonArray();
            for (DialogueRule rule : pool.getB()) {
                JsonObject ruleJson = new JsonObject();
                ruleJson.addProperty("weight", rule.getWeight());
                ruleJson.addProperty("dialogue", rule.getDialogue().toString());
                ruleJson.addProperty("interruptable", rule.isInterruptable());
                dialogues.add(ruleJson);
            }

            JsonUtilities.saveDialoguePool(pool.getA(), dialogues);
        }

        for (ResourceLocation pool : toRemoves) {
            JsonUtilities.deleteDialoguePool(pool);
        }

        ClientOnlyLoadedData.reloadAll();
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    protected void populateEntries() {
        for (Map.Entry<ResourceLocation, List<DialogueRule>> entry : DialoguePoolManager.INSTANCE.getPools().entrySet()) {
            DialoguePoolEntryGui entryGui = createEntry();
            entryGui.load(new Tuple<>(entry.getKey(), entry.getValue()));
            addEntry(entryGui);
        }
    }

    public static void open() {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(new DialogueCreatorUI());
    }

    @Override
    protected void onEntryRemoved(DialoguePoolEntryGui entry) {
        toRemoves.add(entry.create().getA());
    }
}
