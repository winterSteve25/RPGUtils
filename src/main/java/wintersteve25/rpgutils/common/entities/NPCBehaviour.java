package wintersteve25.rpgutils.common.entities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class NPCBehaviour implements INBTSerializable<CompoundNBT> {
    
    private NPCMoveMode prioritisedMoveMode;
    private NPCMoveMode fallbackMoveMode;
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        
    }
}
