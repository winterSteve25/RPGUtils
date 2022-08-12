package wintersteve25.rpgutils.client.ui.dialogue_creator.predicates;

import net.minecraft.client.gui.widget.button.Button;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.selections.nearby_entities.SelectNearbyEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.ModConstants;

import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.CONFIRM_TEXT;
import static wintersteve25.rpgutils.client.ui.components.selection.AbstractSelectionUI.WIDTH;

public class DialoguePredicateConfigurationUI extends BaseUI {
    
    private final Consumer<DialoguePredicate> onSubmit;
    
    public DialoguePredicateConfigurationUI(Consumer<DialoguePredicate> onSubmit) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, SelectNearbyEntity.HEIGHT);
        this.onSubmit = onSubmit;
    }

    @Override
    protected void init() {
        super.init();

        
        
        Button save = new Button(this.x + (WIDTH - 70) / 2, this.y + 145, 70, 20, CONFIRM_TEXT, btn -> {
            // TODO
            onSubmit.accept(null);
        });
        addButton(save);
    }
}