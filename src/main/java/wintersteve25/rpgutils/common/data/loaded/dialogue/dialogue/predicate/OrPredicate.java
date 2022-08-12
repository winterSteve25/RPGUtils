package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate;

import com.google.gson.JsonObject;

public class OrPredicate extends DialoguePredicate {
    private DialoguePredicate condition1;
    private DialoguePredicate condition2;

    protected OrPredicate(JsonObject jsonObject) {
        super(jsonObject);
        JsonObject cond1 = jsonObject.getAsJsonObject("condition1");
        JsonObject cond2 = jsonObject.getAsJsonObject("condition2");
        condition1 = DialoguePredicate.create(cond1.get("name").getAsString(), cond1);
        condition2 = DialoguePredicate.create(cond2.get("name").getAsString(), cond2);
    }

    @Override
    public boolean test() {
        return condition1.test() || condition2.test();
    }
}
