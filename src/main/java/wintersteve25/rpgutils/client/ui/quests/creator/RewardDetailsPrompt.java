package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;

import java.util.function.Consumer;

public class RewardDetailsPrompt extends BaseScreen {

    private RewardButton rewardButton;
    private Consumer<RewardButton> onSubmit;
    private boolean enabled;
    
    private final Button submitButton;
    private final Button cancelButton;
    private final Panel submitCancelButtons;
    
    public RewardDetailsPrompt(Panel panel) {
        parent = panel;
        setSize(176, 200);
        
        submitCancelButtons = new Panel(this) {
            @Override
            public void addWidgets() {
                add(submitButton);
                add(cancelButton);
            }

            @Override
            public void alignWidgets() {
                align(new WidgetLayout.Horizontal(0, 5, 0));
            }
        };
        submitCancelButtons.setSize(105, 20);
        
        submitButton = new SimpleTextButton(submitCancelButtons, new StringTextComponent("Confirm"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                onSubmit.accept(rewardButton);
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
        submitButton.setSize(50, 20);
        
        cancelButton = new SimpleTextButton(submitCancelButtons, new StringTextComponent("Cancel"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                disable();
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
        if (!enabled) return;
        TextField titleText = new TextField(this);
        titleText.setText("Edit Reward");
        add(titleText);
        add(submitCancelButtons);
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
        this.rewardButton = rewardButton;
        this.onSubmit = onSubmit;
        initGui();
    }
}
