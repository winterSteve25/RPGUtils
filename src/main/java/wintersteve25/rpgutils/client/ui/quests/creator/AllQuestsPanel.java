package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import wintersteve25.rpgutils.client.ui.components.PanelScrollbar;

import java.util.ArrayList;
import java.util.List;

public class AllQuestsPanel extends Panel {
    
    private final Panel buttonsPanel;
    private final PanelScrollbar scrollBar;
    private final List<QuestBuilderButton> buttons;
    private final QuestCreationDetailsPanel questDetails;
    
    public AllQuestsPanel(QuestCreatorUI panel, QuestCreationDetailsPanel questDetails) {
        super(panel);
        this.questDetails = questDetails;

        this.buttonsPanel = new Panel(this) {
            @Override
            public void addWidgets() {
                for (QuestBuilderButton button : buttons) {
                    this.add(button);
                }
            }

            @Override
            public void alignWidgets() {
                for (Widget widget : widgets) {
                    widget.setSize(width, 15);
                }

                align(WidgetLayout.VERTICAL);
                
                int scrollHeight = this.align(new WidgetLayout.Vertical(0, 2, 0));
                AllQuestsPanel.this.scrollBar.setMaxValue(scrollHeight);
                AllQuestsPanel.this.scrollBar.setShouldDraw(scrollHeight >= height);
            }
        };
        
        this.scrollBar = new PanelScrollbar(this, buttonsPanel);
        this.scrollBar.setCanAlwaysScroll(true);
        this.scrollBar.setScrollStep(20.0);
        
        this.buttons = new ArrayList<>();
    }

    @Override
    public void addWidgets() {
        add(buttonsPanel);
        add(scrollBar);
        buttonsPanel.setSize(width - 40, height - 20);
        scrollBar.setSize(10, height - 20);
    }

    @Override
    public void alignWidgets() {
        buttonsPanel.setY(5);
        scrollBar.setY(5);
        align(new WidgetLayout.Horizontal(0, 5, 0));
    }

    @Override
    public int getX() {
        return posX;
    }

    @Override
    public int getY() {
        return posY;
    }
    
    public void add(String path) {
        QuestBuilderButton button = new QuestBuilderButton(buttonsPanel, path, questDetails);
        buttons.add(button);
        buttonsPanel.add(button);
        buttonsPanel.alignWidgets();
    }
}
