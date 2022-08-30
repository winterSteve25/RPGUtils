package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.ContextMenu;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.rewards.IReward;

import java.util.ArrayList;
import java.util.List;

public class RewardButton extends NordButton {
    
    private final RewardDetailsPrompt prompt;
    private final ContextMenu contextMenu;
    
    private IReward reward;
    
    public RewardButton(Panel panel, ITextComponent txt, Icon icon, RewardDetailsPrompt prompt, EditRewardsPanel editRewardsPanel) {
        super(panel, txt, icon);
        this.prompt = prompt;
        List<ContextMenuItem> options = new ArrayList<>();
        options.add(new ContextMenuItem(new StringTextComponent("Remove"), Icon.EMPTY, () -> editRewardsPanel.remove(this)));
        contextMenu = new ContextMenu(parent, options);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (mouseButton.isLeft()) {
            playClickSound();
            prompt.enable(this, btn -> {});
            parent.alignWidgets();
        } else if (mouseButton.isRight()) {
            openContextMenu(contextMenu);
        }
    }

    public void setReward(IReward reward) {
        this.reward = reward;
        if (reward == null) return;
        setIcon(reward.rewardIcon());
        setTitle(reward.rewardTitle());
    }

    public IReward getReward() {
        return reward;
    }
}
