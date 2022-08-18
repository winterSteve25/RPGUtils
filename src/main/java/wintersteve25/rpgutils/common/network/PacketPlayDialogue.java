package wintersteve25.rpgutils.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

import java.util.function.Supplier;

public class PacketPlayDialogue implements ModPacket {
    private final ResourceLocation dialogue;

    public PacketPlayDialogue(ResourceLocation dialogue) {
        this.dialogue = dialogue;
    }

    public PacketPlayDialogue(PacketBuffer buffer) {
        this.dialogue = buffer.readResourceLocation();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(dialogue);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DialogueSystem.play(dialogue);
        });
        ctx.get().setPacketHandled(true);
    }
}
