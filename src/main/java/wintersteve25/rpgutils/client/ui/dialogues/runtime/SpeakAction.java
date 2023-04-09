package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import com.github.wintersteve25.tau.components.*;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.utils.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.text.ITextProperties;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueSpeaker;

import java.util.Collections;
import java.util.Map;

public class SpeakAction implements UIComponent, RuntimeDialogueAction, IGuiEventListener, RenderProvider {
    
    private final DialogueSpeaker speaker;
    private final Map<Integer, RuntimeDialogueAction> embeds;
    private final String line;
    private final DialogueContext context;
    
    private int tick;
    private int index;
    private String text;
    private boolean skipped;
    
    private RuntimeDialogueAction embedAction;
    
    public SpeakAction(DialogueSpeaker speaker, String line, Map<Integer, RuntimeDialogueAction> embeds, DialogueContext context) {
        this.speaker = speaker;
        this.embeds = embeds;
        this.line = line;
        this.context = context;

        this.tick = 0;
        this.index = 0;
        this.text = "";
        this.skipped = false;
    }

    @Override
    public boolean isComplete(DialogueUI ui) {
        return skipped;
    }

    @Override
    public boolean tick(DialogueUI ui, float progress) {
        if (index < 0 || index >= line.length()) {
            return false;
        }

        if (embedAction != null) {
            if (embedAction.isComplete(ui)) {
                embedAction.complete(ui);
                embedAction = null;
            } else {
                return embedAction.tick(ui, progress);
            }
        }
        
        tick++;
        if (tick % context.typeInterval == 0) {
            
            if (embeds.containsKey(index)) {
                embedAction = embeds.get(index);
                embedAction.execute(ui);
                embeds.remove(index);
                return false;
            }

            tick = 0;
            text += line.charAt(index);

            index++;
        }
        
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (embedAction != null) {
            return false;
        }
        
        if (index == line.length()) {
            skipped = true;
        } else {
            if (embeds.isEmpty()) {
                text = line;
                index = line.length();
            } else {
                index = Collections.min(embeds.keySet());
                text = line.substring(0, index);
            }
        }
        
        return true;
    }

    @Override
    public UIComponent build(Layout layout) {
        return new Container.Builder()
            .withColor(new Color(0x88000000))
            .withChild(new Padding(
                new Pad.Builder().all(4).build(),
                new Column.Builder()
                    .withAlignment(speaker.getType() == DialogueSpeaker.SpeakerType.PLAYER ? LayoutSetting.START : LayoutSetting.END)
                    .build(
                        new Text.Builder(speaker.getDisplayableName(context.player)),
                        new Spacer(new Vector2i(0, 8)),
                        new Render(this)
                    )
                )
            );
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, int x, int y, int width, int height) {
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        fontRenderer.drawWordWrap(ITextProperties.of(this.text), x, y, width, Color.WHITE.getAARRGGBB());
    }
}