package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates;

import net.minecraft.client.resources.I18n;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.IAttachedUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types.AndPredicateGui;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types.LocalPlayerHealthPredicateGui;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types.NoPredicateGui;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types.OrPredicateGui;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;

import java.util.function.Supplier;

public enum DialoguePredicateType {
    NONE ("rpgutils.dialogues.predicate_types.none", NoPredicateGui::new),
    OR("rpgutils.dialogues.predicate_types.or", OrPredicateGui::new),
    AND("rpgutils.dialogues.predicate_types.and", AndPredicateGui::new),
    LOCAL_PLAYER_HEALTH("rpgutils.dialogues.predicate_types.local_player_health", LocalPlayerHealthPredicateGui::new);

    private final String id;
    private final Supplier<? extends IAttachedUI<DialoguePredicate>> guiCreator;
    
    
    DialoguePredicateType(String id, Supplier<? extends IAttachedUI<DialoguePredicate>> guiCreator) {
        this.id = I18n.get(id);
        this.guiCreator = guiCreator;
    }

    public Supplier<? extends IAttachedUI<DialoguePredicate>> getGuiCreator() {
        return guiCreator;
    }

    @Override
    public String toString() {
        return id;
    }
}
