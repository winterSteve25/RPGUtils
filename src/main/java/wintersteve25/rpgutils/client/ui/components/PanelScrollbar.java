package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;

public class PanelScrollbar extends PanelScrollBar {
    private boolean shouldDraw;
    
    public PanelScrollbar(Panel parent, Panel panel) {
        super(parent, panel);
        shouldDraw = true;
    }

    public void setShouldDraw(boolean shouldDraw) {
        this.shouldDraw = shouldDraw;
    }

    @Override
    public boolean shouldDraw() {
        return shouldDraw;
    }
}
