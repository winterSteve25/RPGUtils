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

import java.util.Map;

public class SpeakAction implements UIComponent, RuntimeDialogueAction, IGuiEventListener, RenderProvider {
    
    private final DialogueSpeaker speaker;
    private final Map<Integer, RuntimeDialogueAction> embeds;
    private final String line;
    private final int typeInterval;
    private final DialogueContext context;
    
    private int tick;
    private int index;
    private String text;
    private boolean skipped;
    
    public SpeakAction(DialogueSpeaker speaker, String line, Map<Integer, RuntimeDialogueAction> embeds, int typeInterval, DialogueContext context) {
        this.speaker = speaker;
        this.embeds = embeds;
        this.line = line;
        this.typeInterval = typeInterval;
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

        tick++;
        if (tick % typeInterval == 0) {
            tick = 0;
            text += line.charAt(index);
            index++;
        }
        
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (index == line.length()) {
            skipped = true;
        } else {
            text = line;
            index = line.length();
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