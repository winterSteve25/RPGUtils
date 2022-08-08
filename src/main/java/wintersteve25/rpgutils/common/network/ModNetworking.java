package wintersteve25.rpgutils.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import wintersteve25.rpgutils.RPGUtils;

public class ModNetworking {
    private static final SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    static {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RPGUtils.MOD_ID, "networking"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .simpleChannel();
    }

    public static void registerMessages() {
        INSTANCE.messageBuilder(PacketOpenDialogueCreator.class, nextID())
                .encoder(PacketOpenDialogueCreator::encode)
                .decoder(PacketOpenDialogueCreator::new)
                .consumer(PacketOpenDialogueCreator::handle)
                .add();

        INSTANCE.messageBuilder(PacketPlaySound.class, nextID())
                .encoder(PacketPlaySound::encode)
                .decoder(PacketPlaySound::new)
                .consumer(PacketPlaySound::handle)
                .add();
    }

    public static SimpleChannel getInstance() {
        return INSTANCE;
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
