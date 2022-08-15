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

import java.util.function.Supplier;

public class PacketPlaySound implements ModPacket {

    private final ResourceLocation sound;
    private final BlockPos position;
    private final SoundCategory category;
    private final float volume;
    private final float pitch;
    
    public PacketPlaySound(SoundEvent sound, BlockPos pos, SoundCategory category, float volume, float pitch) {
        this.sound = sound.getRegistryName();
        this.position = pos;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PacketPlaySound(PacketBuffer buffer) {
        this.sound = buffer.readResourceLocation();
        this.position = buffer.readBlockPos();
        this.category = buffer.readEnum(SoundCategory.class);
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(sound);
        buffer.writeBlockPos(position);
        buffer.writeEnum(category);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            ServerWorld world = player.getLevel();
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(sound);
            if (soundEvent == null) {
                RPGUtils.LOGGER.info("Tried to play audio {} but it is not found in forge registry", sound);
                return;
            }
            world.playSound(null, position, soundEvent, category, volume, pitch);
        });
        ctx.get().setPacketHandled(true);
    }
}
