package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.CycledTypeSelector;
import wintersteve25.rpgutils.client.ui.components.SelectableType;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.ItemReward;

import java.util.ArrayList;
import java.util.function.Consumer;

public class RewardDetailsPrompt extends BaseScreen {
    private RewardButton rewardButton;
    private Consumer<RewardButton> onSubmit;
    private boolean enabled;

    private final RewardTypePanel rewardTypePanel;
    private final SubmitOrCancel submitPanel;

    public RewardDetailsPrompt(Panel panel) {
        parent = panel;
        setSize(176, 100);
        rewardTypePanel = new RewardTypePanel(this);
        submitPanel = new SubmitOrCancel(this, () -> {
            IReward reward = rewardTypePanel.reward;

            if (reward == null || !reward.isValidReward()) {
                return;
            }

            rewardButton.setReward(reward);
            onSubmit.accept(rewardButton);
            disable();

            rewardTypePanel.type.setIcon(Icon.EMPTY);
        }, this::disable);
        submitPanel.setSize(105, 20);
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

    private static class RewardTypePanel extends CycledTypeSelector<String> {

        private IReward reward;

        public RewardTypePanel(RewardDetailsPrompt panel) {
            super(panel, new ArrayList<>());
            setSize(110, 20);

            SelectableType<String> item = new SelectableType<>(reward == null ? Icon.EMPTY : reward.rewardIcon(), new StringTextComponent("Item"), (btn, mouseButton) -> {
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
                            btn.setIcon(reward.rewardIcon());
                        }
                    }).openGui();
                }
            }, "Item");
            options.add(item);
            updateType();
        }

        public void setReward(IReward reward) {
            this.reward = reward;
            if (reward == null) {
                type.setIcon(Icon.EMPTY);
                return;
            }
            type.setIcon(reward.rewardIcon());
        }
    }
}