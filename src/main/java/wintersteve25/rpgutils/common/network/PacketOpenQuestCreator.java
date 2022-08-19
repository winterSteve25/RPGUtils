package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.client.ui.quests.creator.QuestCreatorUI;
import wintersteve25.rpgutils.client.ui.quests.player.QuestUI;

import java.util.function.Supplier;

public class PacketOpenQuestCreator implements ModPacket {
    @Override
    public void encode(PacketBuffer buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(QuestUI::open);
        ctx.get().setPacketHandled(true);
    }
}
