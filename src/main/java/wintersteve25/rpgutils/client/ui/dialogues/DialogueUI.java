package wintersteve25.rpgutils.client.ui.dialogues;

import com.github.wintersteve25.tau.components.*;
import com.github.wintersteve25.tau.components.base.DynamicUIComponent;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.utils.Size;
import net.minecraft.entity.player.PlayerEntity;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

import java.util.Queue;

public class DialogueUI extends DynamicUIComponent {

    private final Queue<RuntimeDialogueAction> lines;
    private RuntimeDialogueAction current;
    
    public DialogueUI(Dialogue dialogue, PlayerEntity player) {
        this.lines = dialogue.getLines(new DialogueContext(player));
        if (lines.isEmpty()) return;
        this.current = lines.remove();
    }

    @Override
    public UIComponent build(Layout layout) {
        if (!(current instanceof UIComponent)) {
            return new Container.Builder()
                .noColor();
        }
        
        return new Align.Builder()
            .withHorizontal(LayoutSetting.CENTER)
            .withVertical((maxLength, componentLength) -> maxLength - (int) (componentLength * 1.1f))
            .build(new Sized(
                Size.percentage(0.7f, 0.3f),
                (UIComponent) current
            ));
    }

    public void tick() {
        if (current == null) {
            return;
        }
        
        if (current.tick(this, 1)) {
            rebuild();
        }
        
        if (current.isComplete(this)) {
            current.complete(this);            
            
            if (!lines.isEmpty()) {
                current = lines.remove();
                current.execute(this);
            } else {
                current = null;
                DialogueSystem.stopDialogue();
            }

            rebuild();
        }
    }
}