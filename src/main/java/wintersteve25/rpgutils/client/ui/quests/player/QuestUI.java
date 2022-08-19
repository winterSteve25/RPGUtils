package wintersteve25.rpgutils.client.ui.quests.player;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;

public class QuestUI extends BaseScreen {

    public QuestUI() {
        
    }

    @Override
    public void addWidgets() {
        this.add(new AvailableQuestsPanel());
    }

    @Override
    public void alignWidgets() {
        super.alignWidgets();
    }

    @Override
    public boolean onInit() {
        return setFullscreen();
    }

    public static void open() {
        new QuestUI().openGui();
    }
}
