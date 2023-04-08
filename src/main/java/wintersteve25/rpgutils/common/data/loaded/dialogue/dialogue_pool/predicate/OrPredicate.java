package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.predicate;

import com.google.gson.JsonObject;

public class OrPredicate extends NestedPredicate {

    public OrPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(condition1, condition2);
    }
    
    protected OrPredicate(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected String name() {
        return "or";
    }

    @Override
    public boolean test() {
        return condition1.test() || condition2.test();
    }
}
