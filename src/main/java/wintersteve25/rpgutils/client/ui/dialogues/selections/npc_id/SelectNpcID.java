package wintersteve25.rpgutils.client.ui.dialogues.selections.npc_id;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.EnterTextConfirmationUI;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectNpcID extends AbstractSelectionUI<NpcIDOption> {

    private static final TranslationTextComponent TITLE = RLHelper.dialogueCreatorComponent("select_npc_id.title");
    private static final TranslationTextComponent HINT = RLHelper.dialogueCreatorComponent("select_npc_id.hint");
    
    public SelectNpcID(boolean allowMultiple, Consumer<List<NpcIDOption>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void init() {
        super.init();

        Button addID = new Button(this.x - 30, this.y + 10, 20, 20, new StringTextComponent("+"), btn -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(new EnterTextConfirmationUI(TITLE, HINT, str -> {
                NpcIDMapping.clientInstance.addMapping(str, ModConstants.INVALID_UUID, null);
                minecraft.setScreen(this);
            }, () -> minecraft.setScreen(this)));
        });
        addButton(addID);

        Button removeID = new Button(this.x - 30, this.y + 35, 20, 20, new StringTextComponent("-"), btn -> {
            List<NpcIDOption> selected = getSelected();
            if (selected.isEmpty()) return;
            NpcIDMapping.clientInstance.removeMapping(selected.get(0).getNpcID(), null);
            minecraft.setScreen(this);
        });
        addButton(removeID);
    }

    @Override
    protected void populateOptions(List<NpcIDOption> list) {
        List<String> ids = new ArrayList<>(NpcIDMapping.clientInstance.getAllIDs());
        
        for (int i = 0; i < ids.size(); i++) {
            list.add(new NpcIDOption(this.x + 10, this.y + 40 + i * 12, this, i, ids.get(i)));
        }
    }

    @Override
    protected NpcIDOption copyFrom(NpcIDOption copyFrom) {
        return new NpcIDOption(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof NpcIDOption;
    }
}