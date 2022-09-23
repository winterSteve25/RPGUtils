package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class LabeledWidget<T extends Widget> extends Panel {
    
    private final TextField label;
    private final T widget;
    
    public LabeledWidget(Panel panel, Function<Panel, T> widget, ITextComponent label) {
        super(panel);
        this.label = new TextField(this);
        this.label.setText(label);
        this.widget = widget.apply(this);
    }

    @Override
    public void addWidgets() {
        widget.setSize(width - label.width - 2, height);
        add(label);
        add(widget);
    }

    @Override
    public void alignWidgets() {
        label.setY((widget.height - label.height) / 2);
        
        if (widget instanceof Panel) {
            ((Panel)widget).alignWidgets();
        }
        
        align(new WidgetLayout.Horizontal(0, 2, 0));
    }

    public T getWidget() {
        return widget;
    }
}
