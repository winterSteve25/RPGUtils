package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.function.Supplier;

public class PacketCurrentQuestStateChanged implements ModPacket{
    
    private final String quest;
    private final boolean started;
    
    public PacketCurrentQuestStateChanged(String quest) {
        this.quest = quest;
        this.started = true;
    }
    
    public PacketCurrentQuestStateChanged() {
        quest = "";
        started = false;
    }
    
    public PacketCurrentQuestStateChanged(PacketBuffer buffer) {
        quest = buffer.readUtf();
        started = buffer.readBoolean();
    }
    
    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(quest);
        buffer.writeBoolean(started);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.getSender();

            if (serverPlayer != null) {
                serverPlayer.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                    if (started) {
                        cap.setCurrentQuest(QuestsManager.INSTANCE.getQuests().get(quest));
                    } else {
                        cap.completeCurrentQuest(serverPlayer, false);
                    }
                });
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (Minecraft.getInstance().player == null) return;
                    Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                        if (started) {
                            cap.setCurrentQuest(QuestsManager.INSTANCE.getQuests().get(quest));
                        } else {
                            cap.completeCurrentQuest(null, false);
                        }
                    });
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}