package wintersteve25.rpgutils.common.quest.data;

import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.common.data.capabilities.base.ICapabilityHolder;

import java.util.*;

public class PlayerQuestProgress implements ICapabilityHolder {
    
    private final List<ResourceLocation> completed;
    private final List<ResourceLocation> known;
    
    public PlayerQuestProgress() {
        this.completed = new ArrayList<>();
        this.known = new ArrayList<>();
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
    }
}
