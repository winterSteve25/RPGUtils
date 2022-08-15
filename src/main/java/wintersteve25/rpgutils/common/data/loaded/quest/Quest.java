package wintersteve25.rpgutils.common.data.loaded.quest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.RewardTypes;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    
    private final ResourceLocation resourceLocation;
    private final TranslationTextComponent title;
    private final TranslationTextComponent description;
    private final List<ResourceLocation> prerequisite;
    private final List<IReward> rewards;
    private final List<IObjective> objectives;
    
    public Quest(ResourceLocation resourceLocation, TranslationTextComponent title, TranslationTextComponent description, List<ResourceLocation> prerequisite, List<IReward> rewards, List<IObjective> objectives) {
        this.resourceLocation = resourceLocation;
        this.title = title;
        this.description = description;
        this.prerequisite = prerequisite;
        this.rewards = rewards;
        this.objectives = objectives;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public TranslationTextComponent getTitle() {
        return title;
    }

    public TranslationTextComponent getDescription() {
        return description;
    }

    public List<ResourceLocation> getPrerequisite() {
        return prerequisite;
    }

    public List<IReward> getRewards() {
        return rewards;
    }

    public List<IObjective> getObjectives() {
        return objectives;
    }

    public static Quest fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        List<IReward> rewards = new ArrayList<>();

        if (jsonObject.has("rewards")) {
            JsonArray rewardsJson = jsonObject.getAsJsonArray("rewards");
            for (JsonElement reward : rewardsJson) {
                JsonObject r = reward.getAsJsonObject();
                rewards.add(RewardTypes.DESERIALIZERS.get(r.get("type").getAsString()).fromJson(r));
            }
        }

        List<ResourceLocation> prerequisites = new ArrayList<>();
        if (jsonObject.has("prerequisites")) {
            JsonArray prerequisitesJson = jsonObject.getAsJsonArray("prerequisites");
            for (JsonElement dep : prerequisitesJson) {
                prerequisites.add(new ResourceLocation(dep.getAsString()));
            }
        }

        String id = resourceLocation.toString().replace(':', '.').replace("/", ".");
        String title = JsonUtilities.getOrDefault(jsonObject, "title", id + ".title");
        String description = JsonUtilities.getOrDefault(jsonObject, "description", id + ".description");

        return new Quest(
                resourceLocation, 
                new TranslationTextComponent(title),
                new TranslationTextComponent(description),
                prerequisites,
                rewards,
                // TODO
                new ArrayList<>());
    }
}
