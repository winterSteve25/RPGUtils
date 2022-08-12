package wintersteve25.rpgutils.client.ui.selections.dialogue_registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.DialogueEditorUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.EnterTextConfirmationUI;
import wintersteve25.rpgutils.client.ui.selections.npc_id.NpcIDOption;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueManager;
import wintersteve25.rpgutils.common.data.loaded.storage.ClientOnlyLoadedData;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectDialogue extends AbstractSelectionUI<DialogueOption> {

    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("create_dialogue");
    private static final TranslationTextComponent NAME_HINT = RLHelper.dialogueCreatorComponent("create_dialogue_name");

    public SelectDialogue(boolean allowMultiple, Consumer<List<DialogueOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void init() {
        super.init();

        Button createDialogue = new Button(this.x - 30, this.y + 10, 20, 20, new StringTextComponent("+"), btn -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(new EnterTextConfirmationUI(TITLE, NAME_HINT, str -> {
                minecraft.setScreen(new DialogueEditorUI(() -> {
                    minecraft.setScreen(this);
                }, new ResourceLocation(str)));
            }, () -> minecraft.setScreen(this)));
        });
        addButton(createDialogue);

        Button removeDialogue = new Button(this.x - 30, this.y + 35, 20, 20, new StringTextComponent("-"), btn -> {
            List<DialogueOption> selected = getSelected();
            if (selected.isEmpty()) return;
            
            for (DialogueOption option : selected) {
                JsonUtilities.deleteDialogue(option.getDialogue().getResourceLocation());
            }

            ClientOnlyLoadedData.reloadAll();
            minecraft.setScreen(this);
            updateConfirmButton();
        });
        addButton(removeDialogue);

        Button edit = new Button(this.x - 30, this.y + 60, 20, 20, new StringTextComponent("E"), btn -> {
            List<DialogueOption> selected = getSelected();
            if (selected.isEmpty()) return;
            Minecraft minecraft = Minecraft.getInstance();
            DialogueOption option = selected.get(0);
            
            minecraft.setScreen(new DialogueEditorUI(() -> {
                minecraft.setScreen(this);
            }, option.getDialogue().getResourceLocation(), option.getDialogue()));
        });
        
        addButton(edit);
    }

    @Override
    protected void populateOptions(List<DialogueOption> list) {
        List<Dialogue> dialogues = new ArrayList<>(DialogueManager.INSTANCE.getDialogues().values());

        for (int i = 0; i < dialogues.size(); i++) {
            list.add(new DialogueOption(this.x + 10, this.y + 40 + i * 12, this, i, dialogues.get(i)));
        }
    }

    @Override
    protected DialogueOption copyFrom(DialogueOption copyFrom) {
        return new DialogueOption(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof DialogueOption;
    }
}