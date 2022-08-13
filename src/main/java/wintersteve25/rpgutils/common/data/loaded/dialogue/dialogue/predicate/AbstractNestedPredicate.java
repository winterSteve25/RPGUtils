package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate;

import com.google.gson.JsonObject;

public abstract class AbstractNestedPredicate extends DialoguePredicate {
    protected final DialoguePredicate condition1;
    protected final DialoguePredicate condition2;

    public AbstractNestedPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(null);
        this.condition1 = condition1;
        this.condition2 = condition2;
    }

    protected AbstractNestedPredicate(JsonObject jsonObject) {
        this(
                DialoguePredicate.create(jsonObject.getAsJsonObject("condition1")),
                DialoguePredicate.create(jsonObject.getAsJsonObject("condition2"))
        );
    }
}
