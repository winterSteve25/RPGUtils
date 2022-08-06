package wintersteve25.rpgutils.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketDialogueCreate;
import wintersteve25.rpgutils.common.utils.ModConstants;

public class DialogueCreator extends Item {
    public DialogueCreator() {
        super(new Properties());
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pTarget, Hand pHand) {
        if (!pPlayer.getCommandSenderWorld().isClientSide()) {
            ItemStack heldItem = pPlayer.getItemInHand(pHand);
            pPlayer.swing(pHand, true);
            ModNetworking.sendToClient(new PacketDialogueCreate(heldItem.getOrCreateTag(), ModConstants.PacketTypes.OPEN_GUI), (ServerPlayerEntity) pPlayer);
        }

        RPGUtils.LOGGER.info(pTarget.getStringUUID());
        
        return super.interactLivingEntity(pStack, pPlayer, pTarget, pHand);
    }

    public static void setDataToItemstack(ServerPlayerEntity player, CompoundNBT data) {
        player.getMainHandItem().setTag(data);
    }
}
