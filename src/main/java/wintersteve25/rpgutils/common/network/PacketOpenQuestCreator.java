package wintersteve25.rpgutils.common.network;

import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.client.ui.quests.QuestCreatorUI;

import java.util.function.Supplier;

public class PacketOpenQuestCreator {
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(QuestCreatorUI::open);
        ctx.get().setPacketHandled(true);
    }
}
