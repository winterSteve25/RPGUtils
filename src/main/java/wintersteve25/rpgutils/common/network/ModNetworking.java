package wintersteve25.rpgutils.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.network.npcid.PacketAddNpcIDMapping;
import wintersteve25.rpgutils.common.network.npcid.PacketRemoveNpcIDMapping;
import wintersteve25.rpgutils.common.network.npcid.PacketSyncNpcIDMapping;

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
                .encoder((data, buffer) -> {})
                .decoder(buffer -> new PacketOpenDialogueCreator())
                .consumer(PacketOpenDialogueCreator::handle)
                .add();

        INSTANCE.messageBuilder(PacketOpenQuestCreator.class, nextID())
                .encoder((data, buffer) -> {})
                .decoder(buffer -> new PacketOpenQuestCreator())
                .consumer(PacketOpenQuestCreator::handle)
                .add();

        INSTANCE.messageBuilder(PacketPlaySound.class, nextID())
                .encoder(PacketPlaySound::encode)
                .decoder(PacketPlaySound::new)
                .consumer(PacketPlaySound::handle)
                .add();

        INSTANCE.messageBuilder(PacketLoadData.class, nextID())
                .encoder((data, buffer) -> {})
                .decoder(buffer -> new PacketLoadData())
                .consumer(PacketLoadData::handle)
                .add();

        INSTANCE.messageBuilder(PacketPlayDialogue.class, nextID())
                .encoder(PacketPlayDialogue::encode)
                .decoder(PacketPlayDialogue::new)
                .consumer(PacketPlayDialogue::handle)
                .add();

        INSTANCE.messageBuilder(PacketSyncNpcIDMapping.class, nextID())
                .encoder(PacketSyncNpcIDMapping::encode)
                .decoder(PacketSyncNpcIDMapping::new)
                .consumer(PacketSyncNpcIDMapping::handle)
                .add();

        INSTANCE.messageBuilder(PacketAddNpcIDMapping.class, nextID())
                .encoder(PacketAddNpcIDMapping::encode)
                .decoder(PacketAddNpcIDMapping::new)
                .consumer(PacketAddNpcIDMapping::handle)
                .add();

        INSTANCE.messageBuilder(PacketRemoveNpcIDMapping.class, nextID())
                .encoder(PacketRemoveNpcIDMapping::encode)
                .decoder(PacketRemoveNpcIDMapping::new)
                .consumer(PacketRemoveNpcIDMapping::handle)
                .add();

        INSTANCE.messageBuilder(PacketSpawnEntity.class, nextID())
                .encoder(PacketSpawnEntity::encode)
                .decoder(PacketSpawnEntity::new)
                .consumer(PacketSpawnEntity::handle)
                .add();

        INSTANCE.messageBuilder(PacketSetType.class, nextID())
                .encoder(PacketSetType::encode)
                .decoder(PacketSetType::new)
                .consumer(PacketSetType::handle)
                .add();

        INSTANCE.messageBuilder(PacketCurrentQuestStateChanged.class, nextID())
                .encoder(PacketCurrentQuestStateChanged::encode)
                .decoder(PacketCurrentQuestStateChanged::new)
                .consumer(PacketCurrentQuestStateChanged::handle)
                .add();
    }

    public static SimpleChannel getInstance() {
        return INSTANCE;
    }

    public static void sendToClient(ModPacket packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(ModPacket packet) {
        INSTANCE.sendToServer(packet);
    }
}
