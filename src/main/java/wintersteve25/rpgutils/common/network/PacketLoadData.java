package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.loaded.storage.ClientOnlyLoadedData;

import java.util.function.Supplier;

public class PacketLoadData implements ModPacket {

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientOnlyLoadedData::reloadAll);
        ctx.get().setPacketHandled(true);
    }
}
