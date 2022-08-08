package wintersteve25.rpgutils.client.ui.dialogue_creator.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.list.AbstractListEntryWidget;
import wintersteve25.rpgutils.client.ui.dialogue_creator.DialoguePoolUI;
import wintersteve25.rpgutils.common.quest.dialogue_pool.DialogueRule;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;

public class DialoguePoolEntryGui extends AbstractListEntryWidget {
    
    private static final TranslationTextComponent DIALOGUES = RLHelper.dialogueCreatorComponent("dialogues");
    private static final TranslationTextComponent POOL_NAME = RLHelper.dialogueCreatorComponent("pool_name");
    
    private TextFieldWidget poolName;
    private Button poolDialogues;
    
    private List<DialogueRule> poolData;
    
    public DialoguePoolEntryGui(int index) {
        super(225, 25, StringTextComponent.EMPTY, index);
    }

    @Override
    public void init(int parentX, int parentY, BaseUI parent) {
        super.init(parentX, parentY, parent);

        String value = poolName == null ? "" : poolName.getValue();
        poolName = new TextFieldWidget(Minecraft.getInstance().font, x + 180, y + 5, 150, 20, POOL_NAME);
        poolName.setMaxLength(50);
        poolName.setVisible(true);
        poolName.setTextColor(16777215);
        poolName.setValue(value);
        parent.addButton(poolName);
        
        poolDialogues = new Button(this.x + 25, this.y + 5, 60, 20, DIALOGUES, btn -> {
            Minecraft.getInstance().setScreen(new DialoguePoolUI(data -> {
                Minecraft.getInstance().setScreen(parent);
                poolData = data;
            }));
        });
        parent.addButton(poolDialogues);
    }

    @Override
    public void remove(BaseUI parent) {
        super.remove(parent);
        parent.removeButton(poolName);
        parent.removeButton(poolDialogues);
    }

    @Override
    public void tick() {
        super.tick();
        poolName.tick();
    }
}