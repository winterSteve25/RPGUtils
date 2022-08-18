package wintersteve25.rpgutils.common.data.loaded.quest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import stdlib.Integers;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.capabilities.base.ICapabilityHolder;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.AbstractTriggeredObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketCompletedQuestObjectives;
import wintersteve25.rpgutils.common.network.PacketCurrentQuestStateChanged;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerQuestProgress implements ICapabilityHolder {
    
    private final List<ResourceLocation> completed;
    private final List<ResourceLocation> known;
    
    private Quest currentQuest;
    private List<IObjective> currentQuestObjectives;
    
    public PlayerQuestProgress() {
        this.completed = new ArrayList<>();
        this.known = new ArrayList<>();
    }

    public List<ResourceLocation> getCompleted() {
        return completed;
    }

    public List<ResourceLocation> getKnown() {
        return known;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest quest) {
        this.currentQuest = quest;
        this.currentQuestObjectives = new ArrayList<>(quest.getObjectives());
        RPGUtils.LOGGER.info("Started quest: {}", currentQuest.getResourceLocation());
    }

    public void completeCurrentQuest(PlayerEntity player) {
        completeCurrentQuest(player, true);
    }

    // do not call, used for networking syncs, use the overload instead
    public void completeCurrentQuest(PlayerEntity player, boolean updateOtherSide) {
        RPGUtils.LOGGER.info("Completed quest: {}", currentQuest.getResourceLocation());
        completed.add(currentQuest.getResourceLocation());
        currentQuest = null;
        currentQuestObjectives = null;
        if (updateOtherSide) {
            PacketCurrentQuestStateChanged packet = new PacketCurrentQuestStateChanged();
            
            if (player.getCommandSenderWorld().isClientSide()) {
                ModNetworking.sendToServer(packet);
            } else {
                ModNetworking.sendToClient(packet, (ServerPlayerEntity) player);
            }
        }
    }

    public void trigger(PlayerEntity player, Object trigger) {
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
                
            } else {
            
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
    
    @Override
    public CompoundNBT write() {
        ListNBT completed = new ListNBT();
        ListNBT known = new ListNBT();
        
        for (ResourceLocation c : this.completed) {
            completed.add(StringNBT.valueOf(c.toString()));
        }
        
        for (ResourceLocation c : this.known) {
            known.add(StringNBT.valueOf(c.toString()));
        }
        
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("completed", completed);
        nbt.put("known", known);
        nbt.putString("active", currentQuest == null ? "" : currentQuest.getResourceLocation().toString());
        
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        INBT c = nbt.get("completed");
        INBT k = nbt.get("known");
        
        if (c instanceof ListNBT && k instanceof ListNBT) {
            ListNBT completed = (ListNBT) c;
            ListNBT known = (ListNBT) k;
            
            for (INBT inbt : completed) {
                this.completed.add(new ResourceLocation(inbt.getAsString()));
            }

            for (INBT inbt : known) {
                this.known.add(new ResourceLocation(inbt.getAsString()));
            }
        }

        String value = nbt.getString("active");
        if (value.isEmpty()) return;
        setCurrentQuest(QuestsManager.INSTANCE.getQuests().get(new ResourceLocation(value)));
    }
}