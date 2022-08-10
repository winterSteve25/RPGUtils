package wintersteve25.rpgutils.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

import java.util.function.Supplier;

public class PacketPlayDialogue {
    private final ResourceLocation dialogue;

    public PacketPlayDialogue(ResourceLocation dialogue) {
        this.dialogue = dialogue;
    }

    public PacketPlayDialogue(PacketBuffer buffer) {
        this.dialogue = buffer.readResourceLocation();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(dialogue);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            DialogueSystem.play(dialogue);
        });
        ctx.get().setPacketHandled(true);
    }
}
