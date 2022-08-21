package wintersteve25.rpgutils.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.FinishDialogueTrigger;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.function.Supplier;

public class PacketFinishedDialogue implements ModPacket {
    
    private final ResourceLocation dialogue;

    public PacketFinishedDialogue(ResourceLocation dialogue) {
        this.dialogue = dialogue;
    }

    public PacketFinishedDialogue(PacketBuffer buffer) {
        this.dialogue = buffer.readResourceLocation();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(dialogue);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
                cap.trigger(player, new FinishDialogueTrigger(dialogue));
            });
        });
        ctx.setPacketHandled(true);
    }
}
