package wintersteve25.rpgutils.common.network.npcid;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.network.ModPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketRemoveNpcIDMapping implements ModPacket {
    private final String npcID;

    public PacketRemoveNpcIDMapping(String npcID) {
        this.npcID = npcID;
    }

    public PacketRemoveNpcIDMapping(PacketBuffer buffer) {
        this.npcID = buffer.readUtf();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(npcID);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                NpcIDMapping.clientInstance.removeMapping(npcID, false, null);
            } else {
                ServerWorld world = (ServerWorld) context.getSender().getCommandSenderWorld();
                NpcIDMapping.get(world).removeMapping(npcID, false, world);
            }
        });
        context.setPacketHandled(true);
    }
}
