package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class CycledTypeSelector<T> extends Panel {
    private final SimpleButton left;
    private final SimpleButton right;
    public final Button type;

    protected final List<SelectableType<T>> options;
    protected int selectedIndex;

    public CycledTypeSelector(Panel panel, List<SelectableType<T>> options) {
        super(panel);
        this.options = options;
        this.selectedIndex = 0;

        left = new SimpleButton(this, StringTextComponent.EMPTY, Icons.LEFT, (btn, mouse) -> {
            if (mouse.isLeft()) {
                playClickSound();
                if (selectedIndex - 1 >= 0) {
                    selectedIndex--;
                } else {
                    selectedIndex = options.size() - 1;
                }
                updateType();
            }
        });
        left.setSize(20, 20);

        right = new SimpleButton(this, StringTextComponent.EMPTY, Icons.RIGHT, (btn, mouse) -> {
            if (mouse.isLeft()) {
                playClickSound();
                if (selectedIndex + 1 < options.size()) {
                    selectedIndex++;
                } else {
                    selectedIndex = 0;
                }
                updateType();
            }
        });
        right.setSize(20, 20);

        SelectableType<T> template = getCurrent();

        type = new SimpleTextButton(this, template.getTitle(), template.getIcon()) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                getCurrent().getOnClicked().accept(type, mouseButton);
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
    }

    @Override
    public void addWidgets() {
        add(left);
        type.setSize(width - 50, 20);
        add(type);
        add(right);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Horizontal(0, 5, 0));
    }

    protected void updateType() {
        SelectableType<T> template = getCurrent();
        type.setIcon(template.getIcon());
        type.setTitle(template.getTitle());
        type.setSize(width - 50, 20);
    }

    protected SelectableType<T> getCurrent() {
        return options.isEmpty() ? new SelectableType<>(null) : options.get(selectedIndex);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        updateType();
    }
}
