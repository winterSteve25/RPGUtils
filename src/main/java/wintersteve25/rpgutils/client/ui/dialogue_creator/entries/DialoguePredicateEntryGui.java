package wintersteve25.rpgutils.client.ui.dialogue_creator.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.list.AbstractListEntryWidget;
import wintersteve25.rpgutils.client.ui.dialogue_creator.predicates.DialoguePredicateConfigurationUI;
import wintersteve25.rpgutils.common.utils.RLHelper;

public class DialoguePredicateEntryGui extends AbstractListEntryWidget {
    private static final TranslationTextComponent CONFIGURATION = RLHelper.dialogueEditorComponent("predicate.configuration");
    
    private Button configurationMenu;
    
    public DialoguePredicateEntryGui(int index) {
        super(225, 25, StringTextComponent.EMPTY, index);
    }

    @Override
    public void init(int parentX, int parentY, BaseUI parent) {
        super.init(parentX, parentY, parent);
        
        configurationMenu = new Button(this.x + 25, this.y + 5, 200, 20, CONFIGURATION, btn -> {
            Minecraft.getInstance().setScreen(new DialoguePredicateConfigurationUI(predicate -> {
                
            }));
        });
        
        parent.addButton(configurationMenu);
    }

    @Override
    public void remove(BaseUI parent) {
        super.remove(parent);
        parent.removeButton(configurationMenu);
    }
}
