package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DialogueRule {
    
    private final float weight;
    private final Map<String, String> dialogueIds;
    private final DialoguePredicate predicate;

    public DialogueRule(float weight, Map<String, String> dialogueIds, DialoguePredicate predicate) {
        this.weight = weight;
        this.dialogueIds = dialogueIds;
        this.predicate = predicate;
    }

    public float getWeight() {
        return weight;
    }

    public String getDialogueId(Locale locale) {
        for (Map.Entry<String, String> entry : dialogueIds.entrySet()) {
            Locale mapLocale = Locale.forLanguageTag(entry.getKey());
            if (mapLocale.getLanguage().equals(locale.getLanguage())) {
                return entry.getValue();
            }
        }

        RPGUtils.LOGGER.error("No dialogue found for locale: {}", locale.getLanguage());
        return null;
    }

    public boolean isValid() {
        return predicate == null || predicate.test();
    }
    
    public static DialogueRule fromJson(JsonObject jsonObject) {
        DialoguePredicate predicate = null;

        if (jsonObject.has("predicate")) {
            JsonObject predicateJson = jsonObject.getAsJsonObject("predicate");
            String predicateName = predicateJson.get("name").getAsString();
            predicate = DialoguePredicate.create(predicateName, predicateJson);
        }
        
        Map<String, String> dialogueIds = new HashMap<>();
        
        if (jsonObject.has("dialogues")) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.get("dialogues").getAsJsonObject().entrySet()) {
                dialogueIds.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        
        return new DialogueRule(
                JsonUtilities.getOrDefault(jsonObject, "weight", 1f),
                dialogueIds,
                predicate
        );
    }
}