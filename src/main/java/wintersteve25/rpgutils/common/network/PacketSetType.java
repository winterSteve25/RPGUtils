package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.entities.NPCType;

import java.util.function.Supplier;

public class PacketSetType implements ModPacket {

    private final int entityId;
    private final NPCType type;

    public PacketSetType(int entityId, NPCType type) {
        this.entityId = entityId;
        this.type = type;
    }

    public PacketSetType(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        this.type = new NPCType(buffer);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(entityId);
        type.writeToBuffer(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            RPGUtils.LOGGER.info("Received PacketSetType");
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity != null) {
                if (entity instanceof NPCEntity) {
                    ((NPCEntity) entity).setNPCType(type);
                } else {
                    RPGUtils.LOGGER.warn("Tried to set client path for non-NPC entity: " + entity.getClass().getName());
                }
            } else {
                Minecraft.getInstance().level.entitiesForRendering().forEach(RPGUtils.LOGGER::info);
            }
        });
        context.get().setPacketHandled(true);
    }
}
