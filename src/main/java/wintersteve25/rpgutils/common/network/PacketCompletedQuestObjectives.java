package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PacketCompletedQuestObjectives implements ModPacket {

    private final int[] indicesToKeep;

    public PacketCompletedQuestObjectives(List<Integer> indicesToKeep) {
        this.indicesToKeep = indicesToKeep.stream().mapToInt(Integer::intValue).toArray();
    }

    public PacketCompletedQuestObjectives(PacketBuffer buffer) {
        indicesToKeep = buffer.readVarIntArray();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarIntArray(indicesToKeep);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.getSender();
            
            if (serverPlayer != null) {
                serverPlayer.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                    cap.completeObjectives(Arrays.stream(indicesToKeep).boxed().collect(Collectors.toList()));
                });
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (Minecraft.getInstance().player == null) return;
                    Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                        cap.completeObjectives(Arrays.stream(indicesToKeep).boxed().collect(Collectors.toList()));
                    });
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}
