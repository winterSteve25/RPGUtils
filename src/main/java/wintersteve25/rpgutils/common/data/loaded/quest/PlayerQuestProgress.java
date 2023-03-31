package wintersteve25.rpgutils.common.data.loaded.quest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.capabilities.base.ICapabilityHolder;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.AbstractTriggeredObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketCompletedQuestObjectives;
import wintersteve25.rpgutils.common.network.PacketCurrentQuestStateChanged;
import wintersteve25.rpgutils.common.network.PacketUpdatePlayerQuestProgress;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PlayerQuestProgress implements ICapabilityHolder {
    
    private final List<ResourceLocation> completedQuests;
    private final List<ResourceLocation> unlockedQuests;
    private final List<Quest> availableQuests;
    
    private Quest currentQuest;
    private List<IObjective> currentQuestObjectives;
    
    public PlayerQuestProgress() {
        this.completedQuests = new ArrayList<>();
        this.unlockedQuests = new ArrayList<>();
        this.availableQuests = new ArrayList<>();
        refreshAvailableQuests();
    }

    public List<ResourceLocation> getCompletedQuests() {
        return completedQuests;
    }

    public List<ResourceLocation> getUnlockedQuests() {
        return unlockedQuests;
    }

    public List<Quest> getAvailableQuests() {
        return availableQuests;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest quest) {
        this.currentQuest = quest;
        this.currentQuestObjectives = quest == null ? null : new ArrayList<>(quest.getObjectives());
     
        if (quest != null) {
            RPGUtils.LOGGER.info("Started quest: {}", currentQuest.getResourceLocation());
        }
    }

    public void completeCurrentQuest(PlayerEntity player) {
        completeCurrentQuest(player, true);
    }

    // do not call, used for networking syncs, use the overload instead
    public void completeCurrentQuest(PlayerEntity player, boolean updateOtherSide) {
        RPGUtils.LOGGER.info("Completed quest: {}", currentQuest.getResourceLocation());
        
        boolean isServer = false;
        
        if (player instanceof ServerPlayerEntity) {
            for (IReward reward : currentQuest.getRewards()) {
                reward.giveReward((ServerPlayerEntity) player);
            }
            
            isServer = true;
        }
        
        completedQuests.add(currentQuest.getResourceLocation());
        currentQuest = null;
        currentQuestObjectives = null;
        
        if (updateOtherSide) {
            PacketCurrentQuestStateChanged packet = new PacketCurrentQuestStateChanged();
            
            if (!isServer) {
                ModNetworking.sendToServer(packet);
            } else {
                ModNetworking.sendToClient(packet, (ServerPlayerEntity) player);
            }
        }
    }

    public void trigger(ServerPlayerEntity player, Object trigger) {
        if (trigger == null) return;
        if (currentQuest == null) return;
        for (IObjective objective : currentQuestObjectives) {
            if (objective instanceof AbstractTriggeredObjective) {
                AbstractTriggeredObjective triggeredObjective = (AbstractTriggeredObjective) objective;
                triggeredObjective.trigger(trigger);
            }
        }
        checkComplete(player);
    }
    
    private void checkComplete(PlayerEntity player) {
        List<IObjective> copy = new ArrayList<>(currentQuestObjectives);
        currentQuestObjectives.removeIf(obj -> obj.isCompleted(player));
        
        if (currentQuestObjectives.isEmpty()) {
            completeCurrentQuest(player);
        } else {
            List<Integer> indicesLeft = new ArrayList<>();
            for (IObjective remaining : currentQuestObjectives) {
                indicesLeft.add(copy.indexOf(remaining));
            }
            PacketCompletedQuestObjectives packet = new PacketCompletedQuestObjectives(indicesLeft);
            
            if (player.getCommandSenderWorld().isClientSide()) {
                ModNetworking.sendToServer(packet);
            } else {
                ModNetworking.sendToClient(packet, (ServerPlayerEntity) player);
            }
        }
    }
    
    // do not call, used for networking syncs
    public void completeObjectives(List<Integer> indicesToKeep) {
        List<IObjective> temp = new ArrayList<>();

        for (int i = 0; i < currentQuestObjectives.size(); i++) {
            if (indicesToKeep.contains(i)) {
                temp.add(currentQuestObjectives.get(i));
            }
        }
        
        currentQuestObjectives = temp;
    }
    
    private void refreshAvailableQuests() {
        availableQuests.clear();
        
        for (Quest quest : QuestsManager.INSTANCE.getQuests().values()) {
            if (canAcceptQuest(quest)) {
                availableQuests.add(quest);
            }
        }
    }
    
    public boolean canAcceptQuest(Quest quest) {
        // if it has prerequisites all prerequisites must be completed
        // if it is unlockable it has to be unlocked
        return (!quest.getPrerequisite().isEmpty() && !new HashSet<>(getCompletedQuests()).containsAll(quest.getPrerequisite())) || !quest.isUnlockable() || getUnlockedQuests().contains(quest.getResourceLocation());
    }
    
    @Override
    public CompoundNBT write() {
        ListNBT completed = new ListNBT();
        ListNBT unlocked = new ListNBT();

        for (ResourceLocation c : this.completedQuests) {
            completed.add(StringNBT.valueOf(c.toString()));
        }
        
        for (ResourceLocation u : this.unlockedQuests) {
            unlocked.add(StringNBT.valueOf(u.toString()));
        }
        
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("completed", completed);
        nbt.put("unlocked", unlocked);
        nbt.putString("active", currentQuest == null ? "" : currentQuest.getResourceLocation().toString());
        
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        INBT c = nbt.get("completed");
        if (c instanceof ListNBT) {
            ListNBT completed = (ListNBT) c;

            for (INBT inbt : completed) {
                this.completedQuests.add(new ResourceLocation(inbt.getAsString()));
            }
        }

        INBT u = nbt.get("unlocked");
        if (u instanceof ListNBT) {
            ListNBT unlocked = (ListNBT) u;
            
            for (INBT inbt : unlocked) {
                this.unlockedQuests.add(new ResourceLocation(inbt.getAsString()));
            }
        }
        
        refreshAvailableQuests();
        
        String value = nbt.getString("active");
        if (value.isEmpty()) return;
        setCurrentQuest(QuestsManager.INSTANCE.getQuests().get(new ResourceLocation(value)));
    }
    
    public static void refreshClient(ServerPlayerEntity player) {
        player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
            ModNetworking.sendToClient(new PacketUpdatePlayerQuestProgress(cap.write()), player);
        });
    }
    
    public static void refreshAvailableQuests(ServerPlayerEntity player) {
        player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(PlayerQuestProgress::refreshAvailableQuests);
    }
}