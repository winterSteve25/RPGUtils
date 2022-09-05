package wintersteve25.rpgutils.client.ui.quests.creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.rpgutils.client.ui.components.LabeledWidget;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

import java.util.List;

public class QuestCreationDetailsPanel extends Panel {

    public final Left left;
    private final Right right;
    
    public QuestBuilderButton button;
    private Quest.Builder builder;
    private boolean enabled;

    public QuestCreationDetailsPanel(QuestCreatorUI panel) {
        super(panel);
        left = new Left(this);
        right = new Right(this);
    }

    @Override
    public void addWidgets() {
        if (!enabled) {
            TextField titleText = new TextField(this);
            titleText.setText("You haven't selected a quest to edit");
            add(titleText);
            return;
        }
        
        left.setSize(185, getScreen().getScreenHeight());
        add(left);
        
        right.setSize(200, getScreen().getScreenHeight());
        add(right);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Horizontal(0, 30, 0));
    }

    public void enable(QuestBuilderButton button) {
        enabled = true;
        this.button = button;
        builder = button.builder;
        left.title.getWidget().setText(builder.getTitle());
        left.description.getWidget().setText(builder.getDescription());
        left.rewardsPanel.builder = new LazyValue<>(() -> builder);
        left.rewardsPanel.refreshRewards();
        left.refreshPrerequisitesTooltips();
        refreshWidgets();
    }

    public void disable() {
        enabled = false;
        button = null;
        builder = null;
        refreshWidgets();
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        if (((QuestCreatorUI)parent).isPromptOpen()) {
            return;
        }
        super.updateMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (((QuestCreatorUI)parent).isPromptOpen()) {
            return false;
        }
        return super.mousePressed(button);
    }

    public static class Left extends Panel {

        private final LabeledWidget<TextBox> title;
        private final LabeledWidget<TextBox> description;
        private final Button selectPrerequisites;
        private final EditRewardsPanel rewardsPanel;

        private TooltipList prerequisitesTooltip;

        public Left(Panel panel) {
            super(panel);

            QuestCreationDetailsPanel parent1 = ((QuestCreationDetailsPanel) this.parent);

            title = new LabeledWidget<>(this, p -> {
                TextBox box = new TextBox(p) {
                    @Override
                    public void onTextChanged() {
                        parent1.builder.setTitle(getText());
                    }
                };
                box.setSize(155, 20);
                box.ghostText = "Title";
                return box;
            }, new StringTextComponent("Title: "));
            title.setSize(200, 20);

            description = new LabeledWidget<>(this, p -> {
                TextBox box = new TextBox(p) {
                    @Override
                    public void onTextChanged() {
                        parent1.builder.setDescription(getText());
                    }
                };
                box.setSize(120, 20);
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
                            parent1.builder.clearPrerequisite();
                            for (SelectQuest.SelectQuestOption questOption : selected) {
                                parent1.builder.addPrerequisite(questOption.getQuest().getResourceLocation());
                            }
                            refreshPrerequisitesTooltips();
                        }, parent1.builder.getResourceLocation()));
                    }
                }
            };
            selectPrerequisites.setSize(185, 20);

            rewardsPanel = new EditRewardsPanel(this, ((QuestCreatorUI) parent1.parent).rewardDetailsPanel, new LazyValue<>(() -> parent1.builder));
            rewardsPanel.setSize(185, 185);
        
            refreshPrerequisitesTooltips();
        }

        @Override
        public void addWidgets() {
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
        public void draw(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
            super.draw(matrixStack, theme, x, y, w, h);
            if (selectPrerequisites.isMouseOver()) {
                prerequisitesTooltip.render(matrixStack, getMouseX(), getMouseY(), getScreen().getGuiScaledWidth(), getScreen().getGuiScaledHeight(), Minecraft.getInstance().font);
            }
        }

        @Override
        public void alignWidgets() {
            align(new WidgetLayout.Vertical(0, 10, 0));
        }

        public void refreshPrerequisitesTooltips() {
            prerequisitesTooltip = new TooltipList();
            Quest.Builder builder = ((QuestCreationDetailsPanel) parent).builder;
            if (builder == null) {
                prerequisitesTooltip.string("No prerequisites");
                return;
            }
            List<ResourceLocation> pres = builder.getPrerequisite();
            if (pres.isEmpty()) {
                prerequisitesTooltip.string("No prerequisites");
            } else {
                for (ResourceLocation pre : pres) {
                    prerequisitesTooltip.string(pre.toString());
                }
            }
        }
    }
    
    private static class Right extends Panel {
        
        public Right(Panel panel) {
            super(panel);
        }

        @Override
        public void addWidgets() {
            
        }

        @Override
        public void alignWidgets() {
            align(WidgetLayout.VERTICAL);
        }
    }
}