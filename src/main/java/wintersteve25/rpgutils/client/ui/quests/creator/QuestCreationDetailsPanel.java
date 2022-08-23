package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.rpgutils.client.ui.components.LabeledWidget;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

public class QuestCreationDetailsPanel extends Panel {
    
    private final Left left;
    
    private Quest.Builder builder;
    private boolean enabled;

    public QuestCreationDetailsPanel(QuestCreatorUI panel) {
        super(panel);
        left = new Left(this);
    }

    @Override
    public void addWidgets() {
        left.setSize(165, getScreen().getScreenHeight());
        add(left);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Horizontal(0, 8, 0));
    }
    
    public void enable(QuestBuilderButton button) {
        enabled = true;
        builder = button.builder;
        left.title.getWidget().setText(builder.getTitle());
        left.description.getWidget().setText(builder.getDescription());
        refreshWidgets();
    }

    private static class Left extends Panel {
        
        private final LabeledWidget<TextBox> title;
        private final LabeledWidget<TextBox> description;
        private final Button selectPrerequisites;
        private final EditRewardsPanel rewardsPanel;

        public Left(Panel panel) {
            super(panel);

            title = new LabeledWidget<>(this, p -> {
                TextBox box = new TextBox(p) {
                    @Override
                    public void onTextChanged() {
                        ((QuestCreationDetailsPanel)Left.this.parent).builder.setTitle(getText());
                    }
                };
                box.setSize(135, 20);
                box.ghostText = "Title";
                return box;
            }, new StringTextComponent("Title: "));
            title.setSize(200, 20);

            description = new LabeledWidget<>(this, p -> {
                TextBox box = new TextBox(p) {
                    @Override
                    public void onTextChanged() {
                        ((QuestCreationDetailsPanel)Left.this.parent).builder.setDescription(getText());
                    }
                };
                box.setSize(100, 20);
                box.ghostText = "Description";
                return box;
            }, new StringTextComponent("Description: "));
            description.setSize(200, 20);

            selectPrerequisites = new SimpleTextButton(this, new StringTextComponent("Prerequisites"), Icon.EMPTY) {
                @Override
                public void onClicked(MouseButton mouseButton) {
                    if (mouseButton.isLeft()) {
                        playClickSound();
                        Minecraft.getInstance().setScreen(new SelectQuest(selected -> {
                            Left.this.parent.parent.openGui();
                            for (SelectQuest.SelectQuestOption questOption : selected) {
                                ((QuestCreationDetailsPanel)Left.this.parent).builder.addPrerequisite(questOption.getQuest().getResourceLocation());
                            }
                        }, ((QuestCreationDetailsPanel)Left.this.parent).builder.getResourceLocation()));
                    }
                }
            };
            selectPrerequisites.setSize(165, 20);

            rewardsPanel = new EditRewardsPanel(this);
            rewardsPanel.setSize(165, 160);
        }
        
        @Override
        public void addWidgets() {
            if (!((QuestCreationDetailsPanel) parent).enabled) {
                TextField titleText = new TextField(this);
                titleText.setText("You haven't selected a quest to edit");
                add(titleText);
                return;
            }

            TextField titleText = new TextField(this);
            titleText.setText(new StringTextComponent("Quest Details").withStyle(TextFormatting.ITALIC));
            titleText.setScale(1.4f);
            add(titleText);
            add(new VerticalSpaceWidget(this, 5));
            add(title);
            add(description);
            add(selectPrerequisites);
            add(rewardsPanel);
        }

        @Override
        public void alignWidgets() {
            align(new WidgetLayout.Vertical(0, 10, 0));
        }
    }
}