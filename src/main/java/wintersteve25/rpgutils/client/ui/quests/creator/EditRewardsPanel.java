package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.PanelScrollbar;

import java.util.ArrayList;
import java.util.List;

public class EditRewardsPanel extends Panel {

    private final Panel scrollPanel;
    private final Panel buttonsPanel;
    private final PanelScrollbar scrollBar;
    private final List<RewardButton> buttons;
    private final Button add;
    
    public EditRewardsPanel(Panel panel) {
        super(panel);
        
        scrollPanel = new Panel(this) {
            @Override
            public void addWidgets() {
                buttonsPanel.setSize(width - 20, height);
                scrollBar.setSize(10, height);
                add(buttonsPanel);
                add(scrollBar);
            }

            @Override
            public void alignWidgets() {
                align(WidgetLayout.HORIZONTAL);
            }
        };
        
        buttonsPanel = new Panel(scrollPanel) {
            @Override
            public void addWidgets() {
                for (Button button : buttons) {
                    this.add(button);
                }
            }

            @Override
            public void alignWidgets() {
                for (Widget widget : widgets) {
                    widget.setSize(width, 15);
                }

                int scrollHeight = this.align(new WidgetLayout.Vertical(0, 2, 0));
                EditRewardsPanel.this.scrollBar.setMaxValue(scrollHeight);
                EditRewardsPanel.this.scrollBar.setShouldDraw(scrollHeight >= height);
            }
        };
        
        scrollBar = new PanelScrollbar(scrollPanel, buttonsPanel);
        scrollBar.setCanAlwaysScroll(true);
        scrollBar.setScrollStep(20.0);

        buttons = new ArrayList<>();

        add = new SimpleTextButton(this, new StringTextComponent("Add Reward"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    RewardButton button = new RewardButton(buttonsPanel, new StringTextComponent("Reward"), Icon.EMPTY);
                    buttons.add(button);
                    buttonsPanel.add(button);
                    buttonsPanel.alignWidgets();
                }
            }
        };
    }

    @Override
    public void addWidgets() {
        add.setSize(width, 20);
        scrollPanel.setSize(width, height - 30);
        add(add);
        add(scrollPanel);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Vertical(0, 8, 0));
    }
}
