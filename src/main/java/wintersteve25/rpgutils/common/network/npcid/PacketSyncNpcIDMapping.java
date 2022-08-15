package wintersteve25.rpgutils.common.network.npcid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.network.ModPacket;

import java.util.function.Supplier;

public class PacketSyncNpcIDMapping implements ModPacket {
    private final CompoundNBT data;

    public PacketSyncNpcIDMapping(CompoundNBT data) {
        this.data = data;
    }

    public PacketSyncNpcIDMapping(PacketBuffer buffer) {
        this.data = buffer.readAnySizeNbt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(data);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                NpcIDMapping.clientInstance.load(data);
            } else {
                NpcIDMapping.get(context.getSender().getCommandSenderWorld()).load(data);
            }
        });
        context.setPacketHandled(true);
    }

}
