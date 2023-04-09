package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.function.Supplier;

public class PacketUpdatePlayerQuestProgress implements ModPacket {
    private final CompoundNBT nbt;
    
    public PacketUpdatePlayerQuestProgress(CompoundNBT nbt) {
        this.nbt = nbt;
    }
    
    public PacketUpdatePlayerQuestProgress(PacketBuffer buffer) {
        this.nbt = buffer.readAnySizeNbt();
    }
    
    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(nbt);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.getSender();
            
            if (serverPlayer != null) {
                serverPlayer.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                    cap.read(nbt);
                });
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (Minecraft.getInstance().player == null) return;
                    Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                        cap.read(nbt);
                    });
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}
