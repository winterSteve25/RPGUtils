package wintersteve25.rpgutils.client.ui.quests.creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.PanelScrollbar;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EditObjectivesPanel extends Panel {

    private final Panel scrollPanel;
    public final Panel buttonsPanel;
    private final PanelScrollbar scrollBar;
    private final List<ObjectiveButton> buttons;
    public LazyValue<Quest.Builder> builder;
    
    private boolean loaded;

    public EditObjectivesPanel(Panel panel, LazyValue<Quest.Builder> builder) {
        super(panel);
        this.builder = builder;

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
                align(new WidgetLayout.Horizontal(0, 10, 0));
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
                EditObjectivesPanel.this.scrollBar.setMaxValue(scrollHeight);
            }

            @Override
            public void drawBackground(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
                theme.drawPanelBackground(matrixStack, x, y, w, h);
            }
        };

        scrollBar = new PanelScrollbar(scrollPanel, buttonsPanel);
        scrollBar.setCanAlwaysScroll(true);
        scrollBar.setScrollStep(20.0);

        buttons = new ArrayList<>();
    }

    @Override
    public void addWidgets() {
        scrollPanel.setSize(width, height - 30);
        add(scrollPanel);

        if (loaded) return;
        Quest.Builder builder = this.builder.get();
        
        for (ObjectiveButton button : refreshRewards()) {
            builder.addObjectives(button);
        }
        builder.clearObjectives();
        
        loaded = true;
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Vertical(0, 8, 0));
    }

    public List<ObjectiveButton> refreshRewards() {
        buttons.clear();
        buttonsPanel.widgets.clear();
        buttonsPanel.alignWidgets();

        for (IObjective objective : builder.get().getObjectives()) {
            ObjectiveButton button = new ObjectiveButton(buttonsPanel, new StringTextComponent("Objective"), Icon.EMPTY, this);
            button.setObjective(objective);
            buttons.add(button);
            buttonsPanel.add(button);
            buttonsPanel.alignWidgets();
        }

        return buttons;
    }

    public void remove(ObjectiveButton objectiveButton) {
        Quest.Builder builder = this.builder.get();
        builder.removeObjectives(objectiveButton);
        builder.removeObjectives(objectiveButton.getObjective());
        buttons.remove(objectiveButton);
        buttonsPanel.widgets.remove(objectiveButton);
        buttonsPanel.alignWidgets();
    }
    
    public void add(ObjectiveButton button) {
        buttons.add(button);
        buttonsPanel.add(button);
        buttonsPanel.alignWidgets();
        builder.get().addObjectives(button);
    }
}