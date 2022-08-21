package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.ui.Panel;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

public class QuestDetailsPanel extends Panel {
    private final Quest.Builder questBuilder;
    
    public QuestDetailsPanel(QuestCreatorUI panel, ResourceLocation resourceLocation) {
        super(panel);
        questBuilder = new Quest.Builder(resourceLocation);
    }

    @Override
    public void addWidgets() {
    }

    @Override
    public void alignWidgets() {

    }
}
