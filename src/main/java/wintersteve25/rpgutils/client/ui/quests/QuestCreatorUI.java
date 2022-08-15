package wintersteve25.rpgutils.client.ui.quests;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;

public class QuestCreatorUI extends BaseScreen {
    @Override
    public void addWidgets() {
    }
    

    public static void open() {
        new QuestCreatorUI().openGui();
    }
}
