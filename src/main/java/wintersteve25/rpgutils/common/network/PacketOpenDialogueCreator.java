package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenDialogueCreator implements ModPacket {

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // TODO 
//        ctx.get().enqueueWork(DialogueCreatorUI::open);
//        ctx.get().setPacketHandled(true);
    }
}
