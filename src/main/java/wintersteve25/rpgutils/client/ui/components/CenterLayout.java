package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;

public class CenterLayout implements WidgetLayout {
    
    public static final CenterLayout CENTER_LAYOUT = new CenterLayout(0);
    private final int spacing;

    public CenterLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public int align(Panel panel) {
        int panelWidth = panel.width;
        int panelHeight = panel.height;

        if (!panel.widgets.isEmpty()) {

            int totalWidgetHeight = 0;
            
            for (Widget widget : panel.widgets) {
                totalWidgetHeight += widget.height + spacing;
            }
            
            totalWidgetHeight -= spacing;         

            int i = (panelHeight - totalWidgetHeight) / 2;
            
            for (Widget widget : panel.widgets) {
                widget.setX((panelWidth - widget.width) / 2);
                widget.setY(i);
                i += widget.height + spacing;
            }
        }
        
        return panelWidth;
    }
}
