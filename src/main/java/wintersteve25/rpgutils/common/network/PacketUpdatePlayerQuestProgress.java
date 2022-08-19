package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
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
            PlayerEntity player;

            if (ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
                player = ctx.getSender();
            } else {
                player = Minecraft.getInstance().player;
            }

            player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                cap.read(nbt);
            });
        });
        ctx.setPacketHandled(true);
    }
}
