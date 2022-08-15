package wintersteve25.rpgutils.common.data.loaded.quest;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.common.data.capabilities.base.ICapabilityHolder;

import java.util.ArrayList;
import java.util.List;

public class PlayerQuestProgress implements ICapabilityHolder {
    
    private final List<ResourceLocation> completed;
    private final List<ResourceLocation> known;
    
    private Quest currentActiveQuest;
    
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

    public Quest getCurrentActiveQuest() {
        return currentActiveQuest;
    }

    public void setCurrentActiveQuest(Quest quest) {
        this.currentActiveQuest = quest;
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
        nbt.putString("active", currentActiveQuest == null ? "" : currentActiveQuest.getResourceLocation().toString());
        
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
        currentActiveQuest = QuestsManager.INSTANCE.getQuests().get(new ResourceLocation(value));
    }
}