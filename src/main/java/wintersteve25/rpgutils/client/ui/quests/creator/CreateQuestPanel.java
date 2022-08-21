package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;

public class CreateQuestPanel extends BaseScreen {

    public boolean enabled;
    
    private final TextField title;
    private final TextBox enterText;
    private final Panel buttons;
    
    public CreateQuestPanel(QuestCreatorUI panel) {
        this.parent = panel;
        title = new TextField(this);
        title.setSize(100, 20);
        title.setText("Create a quest");
        enterText = new TextBox(this);
        enterText.setSize(100, 20);
        enterText.ghostText = "Quest path";
        
        buttons = new Panel(this) {
            @Override
            public void addWidgets() {
                Button confirm = new SimpleTextButton(this, new StringTextComponent("Confirm"), Icon.EMPTY) {
                    @Override
                    public void onClicked(MouseButton mouseButton) {
                        if (mouseButton.isLeft()) {
                            String text = enterText.getText();

                            if (text.isEmpty()) {
                                enterText.ghostText = TextFormatting.RED.toString() + TextFormatting.ITALIC + "Quest path";
                                enterText.setFocused(false);
                                return;
                            }

                            enterText.setText("");
                            panel.addQuest(text);
                            enabled = false;
                            CreateQuestPanel.this.initGui();
                            playClickSound();
                        }
                    }

                    @Override
                    public boolean renderTitleInCenter() {
                        return true;
                    }
                };
                confirm.setSize(45, 20);
                add(confirm);

                Button cancel = new SimpleTextButton(this, new StringTextComponent("Cancel"), Icon.EMPTY) {
                    @Override
                    public void onClicked(MouseButton mouseButton) {
                        if (mouseButton.isLeft()) {
                            enterText.setText("");
                            enabled = false;
                            CreateQuestPanel.this.initGui();
                        }
                    }

                    @Override
                    public boolean renderTitleInCenter() {
                        return true;
                    }
                };
                cancel.setSize(45, 20);
                add(cancel);
            }

            @Override
            public void alignWidgets() {
                align(new WidgetLayout.Horizontal(0, 10, 0));
            }
        };
    }

    @Override
    public void addWidgets() {
        if (!enabled) return;
        add(title);
        add(enterText);
        add(buttons);
        buttons.setSize(100, 20);
    }

    @Override
    public void alignWidgets() {
        align(new CenterLayout(10));
    }

    @Override
    public boolean shouldDraw() {
        return enabled;
    }
}
