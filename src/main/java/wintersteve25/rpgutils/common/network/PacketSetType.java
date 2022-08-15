package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.entities.NPCEntity;

import java.util.function.Supplier;

public class PacketSetType implements ModPacket {

    private final int entityId;
    private final String path;

    public PacketSetType(int entityId, String path) {
        this.entityId = entityId;
        this.path = path;
    }

    public PacketSetType(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        this.path = buffer.readUtf();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeUtf(path);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            RPGUtils.LOGGER.info("Received PacketSetType");
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity != null) {
                if (entity instanceof NPCEntity) {
                    ((NPCEntity) entity).setClientPath(path);
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
