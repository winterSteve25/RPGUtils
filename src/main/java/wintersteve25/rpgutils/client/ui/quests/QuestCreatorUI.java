package wintersteve25.rpgutils.client.ui.quests;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;

public class QuestCreatorUI extends BaseScreen {
    @Override
    public void addWidgets() {
    }

    @Override
    public boolean onInit() {
        return super.onInit();
    }

    @Override
    public void openGuiLater() {
        super.openGuiLater();
    }
    
    public static void open() {
        new QuestCreatorUI().openGui();
    }
}
