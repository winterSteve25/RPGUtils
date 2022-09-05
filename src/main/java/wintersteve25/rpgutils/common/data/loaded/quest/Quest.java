package wintersteve25.rpgutils.common.data.loaded.quest;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.quests.creator.RewardButton;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.ObjectiveTypes;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.RewardTypes;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Quest {
    
    private final ResourceLocation resourceLocation;
    private final TranslationTextComponent title;
    private final TranslationTextComponent description;
    private final ImmutableList<ResourceLocation> prerequisite;
    private final ImmutableList<IReward> rewards;
    private final ImmutableList<IObjective> objectives;
    private final boolean unlockable;
    
    public Quest(ResourceLocation resourceLocation, TranslationTextComponent title, TranslationTextComponent description, ImmutableList<ResourceLocation> prerequisite, ImmutableList<IReward> rewards, ImmutableList<IObjective> objectives, boolean unlockable) {
        this.resourceLocation = resourceLocation;
        this.title = title;
        this.description = description;
        this.prerequisite = prerequisite;
        this.rewards = rewards;
        this.objectives = objectives;
        this.unlockable = unlockable;
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

    public ImmutableList<ResourceLocation> getPrerequisite() {
        return prerequisite;
    }

    public ImmutableList<IReward> getRewards() {
        return rewards;
    }

    public ImmutableList<IObjective> getObjectives() {
        return objectives;
    }

    public boolean isUnlockable() {
        return unlockable;
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

        List<IObjective> objectives = new ArrayList<>();
        if (!jsonObject.has("objectives")) {
            throw new JsonParseException("A quest can not have no objectives!");
        }
        JsonArray objectivesJson = jsonObject.getAsJsonArray("objectives");
        for (JsonElement obj : objectivesJson) {
            JsonObject o = obj.getAsJsonObject();
            objectives.add(ObjectiveTypes.DESERIALIZERS.get(o.get("type").getAsString()).fromJson(o));
        }
        
        String title = JsonUtilities.getOrDefault(jsonObject, "title", defaultTitle(resourceLocation));
        String description = JsonUtilities.getOrDefault(jsonObject, "description", defaultDescription(resourceLocation));
        boolean unlockable = JsonUtilities.getOrDefault(jsonObject, "unlockable", false);
        
        return new Quest(
                resourceLocation, 
                new TranslationTextComponent(title),
                new TranslationTextComponent(description),
                ImmutableList.copyOf(prerequisites),
                ImmutableList.copyOf(rewards),
                ImmutableList.copyOf(objectives),
                unlockable);
    }
    
    private static String defaultTitle(ResourceLocation resourceLocation) {
        return getID(resourceLocation) + ".title";
    }

    private static String defaultDescription(ResourceLocation resourceLocation) {
        return getID(resourceLocation) + ".description";
    }
    
    private static String getID(ResourceLocation resourceLocation) {
        String modid = resourceLocation.getNamespace();
        String path = resourceLocation.getPath();
        return modid + ".quest." + path;
    }
    
    public static class Builder {
        private final List<ResourceLocation> prerequisite;
        private final List<IReward> rewards;
        private final List<IObjective> objectives;

        private ResourceLocation resourceLocation;
        private String title;
        private String description;
        private Optional<Boolean> unlockable;
    
        private final List<RewardButton> rewardButtons;
        
        public Builder(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
            this.prerequisite = new ArrayList<>();
            this.rewards = new ArrayList<>();
            this.objectives = new ArrayList<>();
            this.unlockable = Optional.empty();
            this.rewardButtons = new ArrayList<>();
        }
        
        public Builder(Quest quest) {
            this.resourceLocation = quest.getResourceLocation();
            this.prerequisite = new ArrayList<>(quest.getPrerequisite());
            this.rewards = new ArrayList<>(quest.getRewards());
            this.objectives = new ArrayList<>(quest.getObjectives());
            this.title = quest.getTitle().getKey();
            this.description = quest.getDescription().getKey();
            this.unlockable = Optional.of(quest.isUnlockable());
            this.rewardButtons = new ArrayList<>();
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addPrerequisite(ResourceLocation prerequisite) {
            this.prerequisite.add(prerequisite);
            return this;
        }

        public Builder addRewards(IReward rewards) {
            this.rewards.add(rewards);
            return this;
        }

        public Builder addRewards(RewardButton rewards) {
            this.rewardButtons.add(rewards);
            return this;
        }

        public Builder addObjectives(IObjective objectives) {
            this.objectives.add(objectives);
            return this;
        }

        public Builder removePrerequisite(ResourceLocation prerequisite) {
            this.prerequisite.remove(prerequisite);
            return this;
        }

        public Builder clearPrerequisite() {
            this.prerequisite.clear();
            return this;
        }

        public Builder clearRewards() {
            this.rewards.clear();
            return this;
        }
        
        public Builder removeRewards(IReward rewards) {
            this.rewards.remove(rewards);
            return this;
        }

        public Builder removeObjectives(IObjective objectives) {
            this.objectives.remove(objectives);
            return this;
        }

        public Builder setUnlockable(boolean unlockable) {
            this.unlockable = Optional.of(unlockable);
            return this;
        }

        public Builder rename(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
            return this;
        }
        
        public String getTitle() {
            return title == null ? defaultTitle(resourceLocation) : I18n.get(title);
        }
        
        public String getDescription() {
            return description == null ? defaultDescription(resourceLocation) : I18n.get(description);
        }

        public ResourceLocation getResourceLocation() {
            return resourceLocation;
        }

        public List<ResourceLocation> getPrerequisite() {
            return prerequisite;
        }

        public ImmutableList<IReward> getRewards() {
            List<IReward> temp = new ArrayList<>(rewards);
            for (RewardButton button : rewardButtons) {
                temp.add(button.getReward());
            }
            return ImmutableList.copyOf(temp);
        }

        public Tuple<ResourceLocation, JsonElement> build() throws IllegalArgumentException {
            
            List<IReward> tempRewards = new ArrayList<>(rewards);
            
            for (RewardButton button : rewardButtons) {
                tempRewards.add(button.getReward());
            }
            
            JsonObject jsonObject = new JsonObject();

            if (!tempRewards.isEmpty()) {
                JsonArray array = new JsonArray();
                
                for (IReward reward : tempRewards) {
                    array.add(reward.toJson());
                }
                
                jsonObject.add("rewards", array);
            }

            if (!prerequisite.isEmpty()) {
                JsonArray array = new JsonArray();

                for (ResourceLocation r : prerequisite) {
                    array.add(r.toString());
                }
                
                jsonObject.add("prerequisites", array);
            }
            
            if (objectives.isEmpty()) {
//                throw new IllegalArgumentException("A quest can not have no objectives");
            }
            
            JsonArray objectives = new JsonArray();
            
            for (IObjective objective : this.objectives) {
                objectives.add(objective.toJson());
            }
            
            jsonObject.add("objectives", objectives);

            if (title != null) {
                jsonObject.addProperty("title", title);
            }
            
            if (description != null) {
                jsonObject.addProperty("description", description);
            }
            
            unlockable.ifPresent(aBoolean -> jsonObject.addProperty("unlockable", aBoolean));
            
            return new Tuple<>(resourceLocation, jsonObject);
        }
    }
}
