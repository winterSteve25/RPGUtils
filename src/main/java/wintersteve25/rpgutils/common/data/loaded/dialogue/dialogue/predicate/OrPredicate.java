package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate;

import com.google.gson.JsonObject;

public class OrPredicate extends AbstractNestedPredicate {

    public OrPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(condition1, condition2);
    }
    
    protected OrPredicate(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public boolean test() {
        return condition1.test() || condition2.test();
    }
}
