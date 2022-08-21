package wintersteve25.rpgutils.client.ui.dialogues.components.list;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.buttons.ToggleButton;
import wintersteve25.rpgutils.common.utils.ModConstants;

public abstract class AbstractListEntryWidget extends Widget {
    
    protected int index;
    private ToggleButton toggleButton;

    public AbstractListEntryWidget(int pWidth, int pHeight, ITextComponent pMessage, int index) {
        super(0, 0, pWidth, pHeight, pMessage);
        this.index = index;
    }

    public void init(int parentX, int parentY, BaseUI parent) {
        remove(parent);
        this.x = parentX + 5;
        this.y = parentY + 35 + 30 * (index % EditableListUI.ITEMS_EACH_PAGE);

        boolean initialState = toggleButton != null && toggleButton.isStateTriggered();
        toggleButton = new ToggleButton(this.x, this.y + 8, 12, 12, initialState, btn -> btn.setStateTriggered(!btn.isStateTriggered()));
        toggleButton.initTextureValues(7, 208, 15, 15, ModConstants.Resources.BLANK_SCREEN);
        parent.addButton(toggleButton);
    }
    
    public void remove(BaseUI parent) {
        parent.removeButton(toggleButton);
    }
    
    public void tick() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected() {
        return toggleButton.isStateTriggered();
    }
}
