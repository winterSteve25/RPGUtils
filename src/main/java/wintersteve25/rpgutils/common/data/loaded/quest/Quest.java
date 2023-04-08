package wintersteve25.rpgutils.common.data.loaded.quest;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.ObjectiveTypes;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.RewardTypes;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    
    private final String id;
    private final TranslationTextComponent title;
    private final TranslationTextComponent description;
    private final ImmutableList<String> prerequisite;
    private final ImmutableList<IReward> rewards;
    private final ImmutableList<IObjective> objectives;
    private final boolean lockedByDefault;
    
    public Quest(String id, TranslationTextComponent title, TranslationTextComponent description, ImmutableList<String> prerequisite, ImmutableList<IReward> rewards, ImmutableList<IObjective> objectives, boolean lockedByDefault) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prerequisite = prerequisite;
        this.rewards = rewards;
        this.objectives = objectives;
        this.lockedByDefault = lockedByDefault;
    }

    public String getId() {
        return id;
    }

    public TranslationTextComponent getTitle() {
        return title;
    }

    public TranslationTextComponent getDescription() {
        return description;
    }

    public ImmutableList<String> getPrerequisite() {
        return prerequisite;
    }

    public ImmutableList<IReward> getRewards() {
        return rewards;
    }

    public ImmutableList<IObjective> getObjectives() {
        return objectives;
    }

    public boolean lockedByDefault() {
        return lockedByDefault;
    }

    public static Quest fromJson(String id, JsonObject jsonObject) {
        List<IReward> rewards = new ArrayList<>();

        if (jsonObject.has("rewards")) {
            JsonArray rewardsJson = jsonObject.getAsJsonArray("rewards");
            for (JsonElement reward : rewardsJson) {
                JsonObject r = reward.getAsJsonObject();
                rewards.add(RewardTypes.DESERIALIZERS.get(r.get("type").getAsString()).fromJson(r));
            }
        }

        List<String> prerequisites = new ArrayList<>();
        if (jsonObject.has("prerequisites")) {
            JsonArray prerequisitesJson = jsonObject.getAsJsonArray("prerequisites");
            for (JsonElement pre : prerequisitesJson) {
                prerequisites.add(pre.getAsString());
            }
        }

        List<IObjective> objectives = new ArrayList<>();
        if (!jsonObject.has("objectives")) {
            throw new JsonParseException("A quest can not have no objectives!");
        }
        
        JsonArray objectivesJson = jsonObject.getAsJsonArray("objectives");
        for (JsonElement obj : objectivesJson) {
            JsonObject o = obj.getAsJsonObject();
            objectives.add(ObjectiveTypes.TYPES.get(o.get("type").getAsString()).fromJson(o));
        }
        
        String title = JsonUtilities.getOrDefault(jsonObject, "title", defaultTitle(id));
        String description = JsonUtilities.getOrDefault(jsonObject, "description", defaultDescription(id));
        boolean lockedByDefault = JsonUtilities.getOrDefault(jsonObject, "lockedByDefault", false);
        
        return new Quest(
                id, 
                new TranslationTextComponent(title),
                new TranslationTextComponent(description),
                ImmutableList.copyOf(prerequisites),
                ImmutableList.copyOf(rewards),
                ImmutableList.copyOf(objectives),
                lockedByDefault
        );
    }
    
    private static String defaultTitle(String id) {
        return getID(id) + ".title";
    }

    private static String defaultDescription(String id) {
        return getID(id) + ".description";
    }
    
    private static String getID(String id) {
        return "quest." + id;
    }
}
