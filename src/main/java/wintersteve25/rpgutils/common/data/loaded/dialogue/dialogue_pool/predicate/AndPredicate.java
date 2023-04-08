package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.predicate;

import com.google.gson.JsonObject;

public class AndPredicate extends NestedPredicate {
    public AndPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(condition1, condition2);
    }

    protected AndPredicate(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected String name() {
        return "and";
    }

    @Override
    public boolean test() {
        return condition1.test() && condition2.test();
    }
}
