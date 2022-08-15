package wintersteve25.rpgutils.client.ui.dialogues;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;

import java.util.List;
import java.util.stream.Collectors;

public class DialogueUI extends Screen {
    
    public static final DialogueUI INSTANCE = new DialogueUI();
    
    private final Minecraft minecraft;

    private List<Tuple<DynamicUUID, IDialogueAction>> entries;
    private int currentIndex;
    private boolean active;
    private boolean interruptable;

    private boolean requireInitialization;
    private IDialogueAction currentAction;
    
    public String displayingDialogueText;
    
    public DialogueUI() {
        super(StringTextComponent.EMPTY);
        this.minecraft = Minecraft.getInstance();
    }
    
    private void setDialogue(Dialogue dialogue) {
        this.entries = dialogue.getLines();
        this.currentIndex = 0;
        this.requireInitialization = true;
        this.currentAction = null;
        this.displayingDialogueText = "";
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

        if (minecraft.player == null) return;
        
        pMatrixStack.pushPose();

        Tuple<DynamicUUID, IDialogueAction> entry = entries.get(currentIndex);
        DynamicUUID speaker = entry.getA();
        speaker.setup();
        PlayerEntity player = minecraft.player;
        World world = player.level;
        BlockPos posStart = player.blockPosition().offset(-16, -16, -16);
        BlockPos posEnd = player.blockPosition().offset(16, 16, 16);

        IAnimatedEntity<?> speakerEntity;

        List<Entity> matchingEntities = world.getEntities(null, new AxisAlignedBB(posStart, posEnd)).stream().filter(entity -> entity.getUUID().equals(speaker.getUuid())).collect(Collectors.toList());

        if (matchingEntities.isEmpty()) {
            pMatrixStack.popPose();
            return;
        }

        Entity entity = matchingEntities.get(0);

        if (!(entity instanceof LivingEntity)) {
            pMatrixStack.popPose();
            return;
        }

        speakerEntity = IAnimatedEntity.getOrCreate((LivingEntity) entity);
        
        if (!(entity instanceof PlayerEntity)) {
            entity.lookAt(EntityAnchorArgument.Type.EYES, player.position());
            player.lookAt(EntityAnchorArgument.Type.EYES, entity.position());
        }
        
        currentAction = entry.getB();

        if (requireInitialization) {
            currentAction.initialize(speakerEntity, this, minecraft);
            requireInitialization = false;
        }

        ITextComponent speakerName = speakerEntity.getSelf().getName();
        FontRenderer fontRenderer = minecraft.font;

        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int speakerNameWidth = fontRenderer.width(speakerName);
        int speakerX = (width-speakerNameWidth)/2;
        int speakerY = height - 200;
        int dialogueTextWidth = fontRenderer.width(displayingDialogueText);

        drawString(pMatrixStack, fontRenderer, speakerName, speakerX, speakerY, TextFormatting.WHITE.getColor());
        drawString(pMatrixStack, minecraft.font, displayingDialogueText, (width - dialogueTextWidth) / 2, speakerY + 10, TextFormatting.WHITE.getColor());
        
        currentAction.render(speakerEntity, this, pMatrixStack, minecraft, width, height, speakerX, speakerY, pMouseX, pMouseY);

        if (!minecraft.isPaused()) {
            if (currentAction.act(speakerEntity, this, minecraft, pMouseX, pMouseY)) {
                currentIndex++;
                requireInitialization = true;
                currentAction = null;

                if (currentIndex >= entries.size()) {
                    pMatrixStack.popPose();
                    disable();
                    return;
                }
            }
        }

        pMatrixStack.popPose();
    }

    public boolean isActive() {
        return active;
    }

    public void skip() {
        if (!interruptable) return;
        if (currentAction == null) return;
        currentAction.skip();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void show(Dialogue dialogue, boolean interruptable) {
        INSTANCE.setDialogue(dialogue);
        INSTANCE.interruptable = interruptable;
        INSTANCE.active = true;
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(INSTANCE);
    }
    
    public static void disable() {
        INSTANCE.currentAction = null;
        INSTANCE.active = false;
        Minecraft.getInstance().setScreen(null);
    }
}
