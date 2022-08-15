package wintersteve25.rpgutils.common.network.npcid;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.network.ModPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketAddNpcIDMapping implements ModPacket {
    private final String npcID;
    private final UUID uuid;

    public PacketAddNpcIDMapping(String npcID, UUID uuid) {
        this.npcID = npcID;
        this.uuid = uuid;
    }

    public PacketAddNpcIDMapping(PacketBuffer buffer) {
        this.npcID = buffer.readUtf();
        this.uuid = buffer.readUUID();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(npcID);
        buffer.writeUUID(uuid);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                NpcIDMapping.clientInstance.addMapping(npcID, uuid, false, null);
            } else {
                ServerWorld world = (ServerWorld) context.getSender().getCommandSenderWorld();
                NpcIDMapping.get(world).addMapping(npcID, uuid, false, world);
            }
        });
        context.setPacketHandled(true);
    }

}
