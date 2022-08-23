package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.loaded.storage.ClientOnlyLoadedData;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;

import java.util.function.Supplier;

public class PacketLoadData implements ModPacket {
    @Override
    public void encode(PacketBuffer buffer) {
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        
        if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            ctx.enqueueWork(ClientOnlyLoadedData::reloadAll);
        } else {
            ctx.enqueueWork(ServerOnlyLoadedData::reloadAll);
        }
        
        ctx.setPacketHandled(true);
    }
}
