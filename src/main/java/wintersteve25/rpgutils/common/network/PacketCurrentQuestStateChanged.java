package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.function.Supplier;

public class PacketCurrentQuestStateChanged implements ModPacket {
    
    private final ResourceLocation quest;
    private final boolean started;
    
    public PacketCurrentQuestStateChanged(ResourceLocation quest) {
        this.quest = quest;
        this.started = true;
    }
    
    public PacketCurrentQuestStateChanged() {
        quest = new ResourceLocation("");
        started = false;
    }
    
    public PacketCurrentQuestStateChanged(PacketBuffer buffer) {
        quest = buffer.readResourceLocation();
        started = buffer.readBoolean();
    }
    
    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(quest);
        buffer.writeBoolean(started);
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
                if (started) {
                    cap.setCurrentQuest(QuestsManager.INSTANCE.getQuests().get(quest));
                } else {
                    cap.completeCurrentQuest(player, false);
                }
            });
        });
        ctx.setPacketHandled(true);
    }
}