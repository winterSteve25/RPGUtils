package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.types;

import com.mojang.blaze3d.matrix.MatrixStack;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.AndPredicate;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;

public class AndPredicateGui extends NestedPredicateGui {

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }
    
    @Override
    protected DialoguePredicate save(DialoguePredicate condition1, DialoguePredicate condition2) {
        return new AndPredicate(condition1, condition2);
    }
}
