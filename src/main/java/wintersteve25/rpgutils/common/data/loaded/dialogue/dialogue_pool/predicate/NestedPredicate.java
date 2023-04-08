package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.predicate;

import com.google.gson.JsonObject;

public abstract class NestedPredicate extends DialoguePredicate {
    protected final DialoguePredicate condition1;
    protected final DialoguePredicate condition2;

    public NestedPredicate(DialoguePredicate condition1, DialoguePredicate condition2) {
        super(null);
        this.condition1 = condition1;
        this.condition2 = condition2;
    }

    protected NestedPredicate(JsonObject jsonObject) {
        this(
                create(jsonObject.getAsJsonObject("condition1")),
                create(jsonObject.getAsJsonObject("condition2"))
        );
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name());
        jsonObject.add("condition1", condition1.toJson());
        jsonObject.add("condition2", condition2.toJson());
        return jsonObject;
    }

    protected abstract String name();
}
