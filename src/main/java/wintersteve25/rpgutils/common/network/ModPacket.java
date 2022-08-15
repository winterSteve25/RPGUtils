package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface ModPacket {

    void encode(PacketBuffer buffer);

    void handle(Supplier<NetworkEvent.Context> contextSupplier);
}
