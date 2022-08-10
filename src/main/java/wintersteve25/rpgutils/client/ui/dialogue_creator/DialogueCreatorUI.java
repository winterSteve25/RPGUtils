package wintersteve25.rpgutils.client.ui.dialogue_creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.prompt.ConfirmationUI;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.entries.DialoguePoolEntryGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;

public class DialogueCreatorUI extends EditableListUI<DialoguePoolEntryGui> {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("title");
    private static final TranslationTextComponent EMPTY_POOL_NAME = RLHelper.dialogueCreatorComponent("empty_pool_name");

    protected DialogueCreatorUI() {
        super(TITLE);
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
                Minecraft.getInstance().setScreen(this);
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
        
        Minecraft.getInstance().setScreen(null);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(new DialogueCreatorUI());
    }
}
