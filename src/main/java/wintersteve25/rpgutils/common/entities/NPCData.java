package wintersteve25.rpgutils.common.entities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

public class NPCData implements INBTSerializable<CompoundNBT> {

    private ItemStackHandler inventory;
    private ItemStackHandler armours;
    private ItemStackHandler hands;

    

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
