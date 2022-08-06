package wintersteve25.rpgutils.common.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.rpgutils.client.ui.DialogueCreatorUI;
import wintersteve25.rpgutils.client.ui.DialogueUI;
import wintersteve25.rpgutils.common.items.DialogueCreator;
import wintersteve25.rpgutils.common.quest.dialogue.DialogueManager;
import wintersteve25.rpgutils.common.utils.ModConstants;

import java.util.function.Supplier;

public class PacketDialogueCreate {

    private final CompoundNBT data;
    private final byte type;
    
    public PacketDialogueCreate(CompoundNBT data, byte type) {
        this.data = data;
        this.type = type;
    }

    public PacketDialogueCreate(PacketBuffer buffer) {
        this.data = buffer.readAnySizeNbt();
        this.type = buffer.readByte();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(data);
        buffer.writeByte(type);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (type) {
                case ModConstants.PacketTypes.OPEN_GUI: 
//                    DialogueCreatorUI.open(data);
                    DialogueUI.show(DialogueManager.INSTANCE.getDialogues().get(new ResourceLocation("rpgutils", "some_dialogue")));
                    break;
                case ModConstants.PacketTypes.SET_DATA: 
                    DialogueCreator.setDataToItemstack(ctx.get().getSender(), data);
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
