package wintersteve25.rpgutils.client.ui.dialogues;

import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer;

public class DialogueRenderer extends ScreenUIRenderer {
    public DialogueRenderer(UIComponent uiComponent) {
        super(uiComponent, false);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
