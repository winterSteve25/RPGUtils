package wintersteve25.rpgutils.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketPlayDialogue implements ModPacket {
    private final String dialoguePoolId;
    private final UUID player;
    
    public PacketPlayDialogue(String dialoguePoolId, PlayerEntity player) {
        this.dialoguePoolId = dialoguePoolId;
        this.player = player.getUUID();
    }

    public PacketPlayDialogue(PacketBuffer buffer) {
        this.dialoguePoolId = buffer.readUtf();
        this.player = buffer.readUUID();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(dialoguePoolId);
        buffer.writeUUID(player);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> DialogueSystem.play(Minecraft.getInstance().level.getPlayerByUUID(player), dialoguePoolId));
        ctx.setPacketHandled(true);
    }
}
