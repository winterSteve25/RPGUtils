package wintersteve25.rpgutils.common.network;

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

import java.util.function.Supplier;

public class PacketSpawnEntity implements ModPacket {
    
    private final ResourceLocation entityType;
    private final BlockPos position;
    private final String npcID;
    
    public PacketSpawnEntity(ResourceLocation entityType, BlockPos position, String npcID) {
        this.entityType = entityType;
        this.position = position;
        this.npcID = npcID;
    }
    
    public PacketSpawnEntity(PacketBuffer buffer) {
        this.entityType = buffer.readResourceLocation();
        this.position = buffer.readBlockPos();
        this.npcID = buffer.readUtf();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(entityType);
        buffer.writeBlockPos(position);
        buffer.writeUtf(npcID);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            ServerWorld world = player.getLevel();
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(entityType);
            
            if (type == null) {
                RPGUtils.LOGGER.error("Tried to spawn entity {} but it is not found in forge registry", entityType);
                return;
            }
            
            Entity entity = type.create(world);
            if (entity == null) {
                RPGUtils.LOGGER.error("Tried to spawn entity {} but failed to create entity instance", entityType);
                return;
            }
            entity.setPos(position.getX(), position.getY(), position.getZ());
            
            if (!npcID.isEmpty()) {
                NpcIDMapping.get(world).addMapping(npcID, entity.getUUID(), world);
            }
            
            world.addFreshEntity(entity);
        });
        ctx.get().setPacketHandled(true);
    }
}
