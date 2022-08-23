package wintersteve25.rpgutils.client.ui.quests.creator;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import wintersteve25.rpgutils.client.ui.components.PanelScrollbar;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

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

                int scrollHeight = this.align(new WidgetLayout.Vertical(0, 2, 0));
                AllQuestsPanel.this.scrollBar.setMaxValue(scrollHeight);
                AllQuestsPanel.this.scrollBar.setShouldDraw(scrollHeight >= height);
            }
        };
        
        this.scrollBar = new PanelScrollbar(this, buttonsPanel);
        this.scrollBar.setCanAlwaysScroll(true);
        this.scrollBar.setScrollStep(20.0);
        
        this.buttons = new ArrayList<>();
        
        for (Quest quest : QuestsManager.INSTANCE.getQuests().values()) {
            buttons.add(new QuestBuilderButton(buttonsPanel, questDetails, this, new Quest.Builder(quest)));
        }
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
        
        buttonsPanel.alignWidgets();
        align(new WidgetLayout.Horizontal(0, 5, 0));
    }

    @Override
    public void onClosed() {
        JsonUtilities.deleteAllFiles("quests");
        for (QuestBuilderButton button : buttons) {
            save(button, false);
        }
        JsonUtilities.reloadAllDataFromClient();
        
        super.onClosed();
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
        QuestBuilderButton button = new QuestBuilderButton(buttonsPanel, path, questDetails, this);
        buttons.add(button);
        buttonsPanel.add(button);
        buttonsPanel.alignWidgets();
    }
    
    public void remove(QuestBuilderButton button) {
        buttons.remove(button);
        buttonsPanel.widgets.remove(button);
        buttonsPanel.alignWidgets();
    }
    
    public void select(QuestBuilderButton clicked) {
        for (QuestBuilderButton button : buttons) {
            if (button == clicked) continue;
            save(button, true);
        }
    }
    
    private void save(QuestBuilderButton button, boolean reload) {
        Tuple<ResourceLocation, JsonElement> quest = button.builder.build();
        JsonUtilities.saveQuest(quest.getA(), quest.getB(), reload);
    }
}
