package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.ItemReward;

import java.util.function.Consumer;

public class RewardDetailsPrompt extends BaseScreen {

    private RewardButton rewardButton;
    private Consumer<RewardButton> onSubmit;
    private boolean enabled;
    
    private final RewardTypePanel rewardTypePanel;
    private final SubmitPanel submitPanel;
    
    public RewardDetailsPrompt(Panel panel) {
        parent = panel;
        setSize(176, 100);
        rewardTypePanel = new RewardTypePanel(this);
        submitPanel = new SubmitPanel(this, () -> {
            if (rewardTypePanel.reward == null) {
                disable();
                return;
            }
            
            rewardButton.setReward(rewardTypePanel.reward);
            onSubmit.accept(rewardButton);
            disable();
        });
    }

    @Override
    public void addWidgets() {
        if (!enabled) return;
        TextField titleText = new TextField(this);
        titleText.setText("Edit Reward");
        add(titleText);
        add(rewardTypePanel);
        add(submitPanel);
    }

    @Override
    public void alignWidgets() {
        align(new CenterLayout(10));
    }
    
    @Override
    public boolean shouldDraw() {
        return enabled;
    }
    
    public void disable() {
        enabled = false;
        rewardButton = null;
        initGui();
    }

    public void enable(RewardButton rewardButton, Consumer<RewardButton> onSubmit) {
        enabled = true;
        rewardTypePanel.setReward(rewardButton.getReward());
        this.rewardButton = rewardButton;
        this.onSubmit = onSubmit;
        initGui();
    }
    
    private static class RewardTypePanel extends Panel {

        private final SimpleButton left;
        private final SimpleButton right;
        private final Button type;
        
        private IReward reward;
        
        public RewardTypePanel(RewardDetailsPrompt panel) {
            super(panel);
            setSize(110, 20);
            
            left = new SimpleButton(this, StringTextComponent.EMPTY, Icons.LEFT, (btn, mouse) -> {});
            left.setSize(20, 20);
            
            right = new SimpleButton(this, StringTextComponent.EMPTY, Icons.RIGHT, (btn, mouse) -> {});
            right.setSize(20, 20);
            
            type = new SimpleTextButton(this, new StringTextComponent("Item"), reward == null ? ItemIcon.EMPTY : reward.rewardIcon()) {
                @Override
                public void onClicked(MouseButton mouseButton) {
                    if (mouseButton.isLeft()) {
                        playClickSound();
                        
                        ItemStackConfig config = new ItemStackConfig(true, false);
                        if (reward instanceof ItemReward) {
                            config.setCurrentValue(((ItemReward) reward).getItem());
                        }
                        
                        new SelectItemStackScreen(config, selected -> {
                            panel.parent.openGui();
                            if (selected) {
                                reward = new ItemReward(config.value);
                                type.setIcon(reward.rewardIcon());
                            }
                        }).openGui();
                    }
                }

                @Override
                public boolean renderTitleInCenter() {
                    return true;
                }
            };
            type.setSize(60, 20);
        }

        @Override
        public void addWidgets() {
            add(left);
            add(type);
            add(right);
        }

        @Override
        public void alignWidgets() {
            align(new WidgetLayout.Horizontal(0, 5, 0));
        }

        public void setReward(IReward reward) {
            this.reward = reward;
            if (reward == null) return;
            type.setIcon(reward.rewardIcon());
        }
    }
    
    private static class SubmitPanel extends Panel {
        private final Button submitButton;
        private final Button cancelButton;

        public SubmitPanel(RewardDetailsPrompt panel, Runnable onSubmit) {
            super(panel);
            setSize(105, 20);

            submitButton = new SimpleTextButton(this, new StringTextComponent("Confirm"), Icon.EMPTY) {
                @Override
                public void onClicked(MouseButton mouseButton) {
                    if (mouseButton.isLeft()) {
                        playClickSound();
                        onSubmit.run();
                    }
                }

                @Override
                public boolean renderTitleInCenter() {
                    return true;
                }
            };
            submitButton.setSize(50, 20);

            cancelButton = new SimpleTextButton(this, new StringTextComponent("Cancel"), Icon.EMPTY) {
                @Override
                public void onClicked(MouseButton mouseButton) {
                    if (mouseButton.isLeft()) {
                        playClickSound();
                        panel.disable();
                    }
                }

                @Override
                public boolean renderTitleInCenter() {
                    return true;
                }
            };
            cancelButton.setSize(50, 20);
        }

        @Override
        public void addWidgets() {
            add(submitButton);
            add(cancelButton);
        }

        @Override
        public void alignWidgets() {
            align(new WidgetLayout.Horizontal(0, 5, 0));
        }
    }
}