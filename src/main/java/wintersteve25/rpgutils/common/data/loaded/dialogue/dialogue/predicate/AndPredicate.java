package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate;

import com.google.gson.JsonObject;

public class AndPredicate extends AbstractNestedPredicate {
    public AndPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(condition1, condition2);
    }

    protected AndPredicate(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public boolean test() {
        return condition1.test() && condition2.test();
    }
}
