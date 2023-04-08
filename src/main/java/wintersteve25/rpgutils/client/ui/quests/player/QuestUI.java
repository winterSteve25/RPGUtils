package wintersteve25.rpgutils.client.ui.quests.player;

import com.github.wintersteve25.tau.components.Row;
import com.github.wintersteve25.tau.components.Sized;
import com.github.wintersteve25.tau.components.base.DynamicUIComponent;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer;
import com.github.wintersteve25.tau.utils.FlexSizeBehaviour;
import com.github.wintersteve25.tau.utils.Size;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import wintersteve25.rpgutils.common.data.loaded.quest.PlayerQuestProgress;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.registry.ModCapabilities;

import java.util.ArrayList;
import java.util.List;

public class QuestUI extends DynamicUIComponent {

    private final List<Quest> allAvailableQuests;
    private Quest selectedQuest;
    
    public QuestUI(PlayerEntity player) {
        allAvailableQuests = player.getCapability(ModCapabilities.PLAYER_QUEST).map(PlayerQuestProgress::getAvailableQuests).orElse(new ArrayList<>());
    }
    
    @Override
    public UIComponent build(Layout layout) {
        return new Row.Builder()
            .withSizeBehaviour(FlexSizeBehaviour.MAX)
            .build(
                new Sized(
                    Size.percentage(0.2f, 1f),
                    new AllAvailableQuests(this::setSelectedQuest, allAvailableQuests)
                ),
                new Sized(
                    Size.percentage(0.8f, 1f),
                    new QuestDetail(selectedQuest)
                )
            );
    }

    private void setSelectedQuest(Quest selectedQuest) {
        this.selectedQuest = selectedQuest;
        rebuild();
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ScreenUIRenderer(new QuestUI(Minecraft.getInstance().player), false));
    }
}
