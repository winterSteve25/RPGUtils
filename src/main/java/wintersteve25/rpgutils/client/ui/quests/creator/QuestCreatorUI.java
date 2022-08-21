package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;

public class QuestCreatorUI extends BaseScreen {
    
    private final AllQuestsPanel questsPanel;
    private final CreateQuestPanel createQuestPanel;
    private final QuestCreationDetailsPanel detailsPanel;
    
    private QuestCreatorUI() {
        detailsPanel = new QuestCreationDetailsPanel(this);
        createQuestPanel = new CreateQuestPanel(this);
        questsPanel = new AllQuestsPanel(this, detailsPanel);
    }
    
    @Override
    public void addWidgets() {
        add(questsPanel);
        questsPanel.setPosAndSize(10, 40, 160, getScreen().getGuiScaledHeight() - 40);
        add(createQuestPanel);
        createQuestPanel.setSize(176, 100);
        add(detailsPanel);
        detailsPanel.setPosAndSize(170, 15, getScreen().getScreenWidth() - 180, getScreen().getGuiScaledHeight() - 40);
        
        Button button = new SimpleTextButton(this, new StringTextComponent("New Quest"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    createQuestPanel.enabled = true;
                    createQuestPanel.initGui();
                }
            }
        };
        add(button);
        button.setPosAndSize(10, 15, 135, 20);
    }
    
    @Override
    public void alignWidgets() {
        questsPanel.alignWidgets();
        createQuestPanel.alignWidgets();
        detailsPanel.alignWidgets();
    }

    @Override
    public boolean onInit() {
        createQuestPanel.initGui();
        return setFullscreen();
    }
    
    public void addQuest(String path) {
        questsPanel.add(path);
    }
    
    public static void open() {
        new QuestCreatorUI().openGui();
    }
}
