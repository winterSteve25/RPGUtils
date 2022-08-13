package wintersteve25.rpgutils.common.network;

import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.DialogueCreatorUI;

import java.util.function.Supplier;

public class PacketOpenDialogueCreator {
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(DialogueCreatorUI::open);
        ctx.get().setPacketHandled(true);
    }
}
