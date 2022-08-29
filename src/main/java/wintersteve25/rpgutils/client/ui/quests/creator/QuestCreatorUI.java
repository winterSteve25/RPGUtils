package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.rpgutils.RPGUtils;

public class QuestCreatorUI extends BaseScreen {

    private final AllQuestsPanel questsPanel;
    private final QuestCreationDetailsPanel detailsPanel;
    private final TextInputPrompt createQuestPrompt;
    public final RenameQuestPrompt renameQuestPrompt;
    public final RewardDetailsPrompt rewardDetailsPanel;
    
    private QuestCreatorUI() {
        rewardDetailsPanel = new RewardDetailsPrompt(this);
        detailsPanel = new QuestCreationDetailsPanel(this);
        questsPanel = new AllQuestsPanel(this, detailsPanel);
        
        createQuestPrompt = new TextInputPrompt(this, new StringTextComponent("Create Quest"), "Quest path", (prompt, btn) -> {
            if (btn.isLeft()) {
                String text = prompt.enterText.getText();
                
                if (text.isEmpty()) {
                    prompt.enterText.ghostText = TextFormatting.RED.toString() + TextFormatting.ITALIC + "Quest path";
                    prompt.enterText.setFocused(false);
                    return;
                }

                prompt.enterText.setText("");
                QuestCreatorUI.this.addQuest(text.replace(" ", "_"));
                prompt.disable();
                playClickSound();
            }
        }, (prompt, btn) -> {
            if (btn.isLeft()) {
                prompt.enterText.setText("");
                prompt.disable();
                playClickSound();
            }
        });

        renameQuestPrompt = new RenameQuestPrompt(this, new StringTextComponent("Rename Quest"), "Quest path", (prompt, btn) -> {
            if (btn.isLeft()) {
                String text = prompt.enterText.getText().replace(" ", "_").toLowerCase();

                if (text.isEmpty()) {
                    prompt.enterText.ghostText = TextFormatting.RED.toString() + TextFormatting.ITALIC + "Quest path";
                    prompt.enterText.setFocused(false);
                    return;
                }
                
                QuestBuilderButton currentRenameButton = ((RenameQuestPrompt)prompt).button;
                ResourceLocation newLocation = new ResourceLocation(RPGUtils.MOD_ID, text);
                
                // refactor all quests that used the previous name as a dependency
                for (QuestBuilderButton button : questsPanel.buttons) {
                    if (button == currentRenameButton) continue;
                    button.builder.getPrerequisite().replaceAll(pre -> pre.equals(currentRenameButton.builder.getResourceLocation()) ? newLocation : pre);
                }
                
                currentRenameButton.builder.rename(newLocation);
                currentRenameButton.setTitle(new StringTextComponent(text));
                
                prompt.enterText.setText("");
                prompt.disable();
                playClickSound();
                detailsPanel.left.refreshPrerequisitesTooltips();
                questsPanel.alignWidgets();
            }
        }, (prompt, btn) -> {
            if (btn.isLeft()) {
                prompt.enterText.setText("");
                prompt.disable();
                playClickSound();
            }
        });
    }

    @Override
    public void addWidgets() {
        add(questsPanel);
        questsPanel.setPosAndSize(10, 40, 160, getScreen().getGuiScaledHeight() - 40);
        add(detailsPanel);
        detailsPanel.setPosAndSize(170, 15, getScreen().getScreenWidth() - 180, getScreen().getGuiScaledHeight() - 40);
        add(createQuestPrompt);
        createQuestPrompt.setSize(176, 100);
        add(renameQuestPrompt);
        renameQuestPrompt.setSize(176, 100);
        add(rewardDetailsPanel);
        
        Button button = new SimpleTextButton(this, new StringTextComponent("New Quest"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    createQuestPrompt.enable();
                }
            }
        };
        add(button);
        button.setPosAndSize(10, 15, 135, 20);
    }

    public boolean isPromptOpen() {
        if (rewardDetailsPanel.shouldDraw()) {
            return true;
        }

        if (createQuestPrompt.shouldDraw()) {
            return true;
        }

        return renameQuestPrompt.shouldDraw();
    }
    
    @Override
    public void alignWidgets() {
        questsPanel.alignWidgets();
        detailsPanel.alignWidgets();
        createQuestPrompt.alignWidgets();
        renameQuestPrompt.alignWidgets();
        rewardDetailsPanel.alignWidgets();
    }

    @Override
    public boolean onInit() {
        createQuestPrompt.initGui();
        renameQuestPrompt.initGui();
        rewardDetailsPanel.initGui();
        return setFullscreen();
    }
    
    public void addQuest(String path) {
        questsPanel.add(path);
    }

    public static void open() {
        new QuestCreatorUI().openGui();
    }
}
