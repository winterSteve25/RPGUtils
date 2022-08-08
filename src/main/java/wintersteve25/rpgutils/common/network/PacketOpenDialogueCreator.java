package wintersteve25.rpgutils.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.client.ui.dialogue_creator.DialogueCreatorUI;
import wintersteve25.rpgutils.client.ui.dialogue_creator.DialogueEditorUI;

import java.util.function.Supplier;

public class PacketOpenDialogueCreator {

    public PacketOpenDialogueCreator() {
    }

    public PacketOpenDialogueCreator(PacketBuffer buffer) {
    }

    public void encode(PacketBuffer buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(DialogueCreatorUI::open);
        ctx.get().setPacketHandled(true);
    }
}
