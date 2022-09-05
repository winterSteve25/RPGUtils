package wintersteve25.rpgutils.client.ui.quests.creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.PanelScrollbar;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;

import java.util.ArrayList;
import java.util.List;

public class EditRewardsPanel extends Panel {

    private final Panel scrollPanel;
    private final Panel buttonsPanel;
    private final PanelScrollbar scrollBar;
    private final List<RewardButton> buttons;
    private final Button add;
    public LazyValue<Quest.Builder> builder;
    private final RewardDetailsPrompt rewardDetailsPanel;

    private boolean loaded;
    
    public EditRewardsPanel(Panel panel, RewardDetailsPrompt rewardDetailsPanel, LazyValue<Quest.Builder> builder) {
        super(panel);
        this.builder = builder;
        this.rewardDetailsPanel = rewardDetailsPanel;
        
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
                EditRewardsPanel.this.scrollBar.setMaxValue(scrollHeight);
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

        add = new SimpleTextButton(this, new StringTextComponent("Add Reward"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    RewardButton button = new RewardButton(buttonsPanel, new StringTextComponent("Reward"), Icon.EMPTY, rewardDetailsPanel, EditRewardsPanel.this);
                    rewardDetailsPanel.enable(button, b -> {
                        buttons.add(b);
                        buttonsPanel.add(b);
                        buttonsPanel.alignWidgets();
                        builder.get().addRewards(b);
                    });
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
        
        if (loaded) return;
        for (RewardButton button : refreshRewards()) {
            builder.get().addRewards(button);
        }
        builder.get().clearRewards();
        loaded = true;
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Vertical(0, 8, 0));
    }
    
    public List<RewardButton> refreshRewards() {
        buttons.clear();
        buttonsPanel.widgets.clear();
        buttonsPanel.alignWidgets();
        
        for (IReward reward : builder.get().getRewards()) {
            RewardButton button = new RewardButton(buttonsPanel, new StringTextComponent("Reward"), Icon.EMPTY, rewardDetailsPanel, EditRewardsPanel.this);
            button.setReward(reward);
            buttons.add(button);
            buttonsPanel.add(button);
            buttonsPanel.alignWidgets();
        }
        
        return buttons;
    }

    public void remove(RewardButton rewardButton) {
        builder.get().removeRewards(rewardButton.getReward());
        buttons.remove(rewardButton);
        buttonsPanel.widgets.remove(rewardButton);
        buttonsPanel.alignWidgets();
    }
}
