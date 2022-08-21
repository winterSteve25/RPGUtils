package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketPlayDialogue implements ModPacket {
    private final ResourceLocation dialogue;
    private final UUID player;
    
    public PacketPlayDialogue(ResourceLocation dialogue, PlayerEntity player) {
        this.dialogue = dialogue;
        this.player = player.getUUID();
    }

    public PacketPlayDialogue(PacketBuffer buffer) {
        this.dialogue = buffer.readResourceLocation();
        this.player = buffer.readUUID();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(dialogue);
        buffer.writeUUID(player);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> DialogueSystem.play(Minecraft.getInstance().level.getPlayerByUUID(player), dialogue));
        ctx.setPacketHandled(true);
    }
}
