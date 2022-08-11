package wintersteve25.rpgutils.client.ui.selections.dialogue_registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.DialogueEditorUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.EnterTextConfirmationUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectDialogue extends AbstractSelectionUI<DialogueOption> {

    public SelectDialogue(boolean allowMultiple, Consumer<List<DialogueOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void init() {
        super.init();

        Button createDialogue = new Button(this.x - 30, this.y + 10, 20, 20, new StringTextComponent("+"), btn -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(new EnterTextConfirmationUI(str -> {
                minecraft.setScreen(new DialogueEditorUI(() -> {
                    minecraft.setScreen(this);
                }, new ResourceLocation(str)));
            }, () -> minecraft.setScreen(this)));
        });
        addButton(createDialogue);
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