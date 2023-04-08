package wintersteve25.rpgutils.client.ui.quests.player;

import com.github.wintersteve25.tau.components.*;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.utils.Pad;
import com.github.wintersteve25.tau.utils.Size;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AllAvailableQuests implements UIComponent {
    
    private final Consumer<Quest> onSelectQuest;
    private final List<Quest> allAvailableQuests;

    public AllAvailableQuests(Consumer<Quest> onSelectQuest, List<Quest> allAvailableQuests) {
        this.onSelectQuest = onSelectQuest;
        this.allAvailableQuests = allAvailableQuests;
    }

    @Override
    public UIComponent build(Layout layout) {
        return new Container.Builder()
            .withColor(layout.getColorScheme().secondaryBackgroundColor())
            .withChild(new ListView.Builder()
                .withSpacing(5)
                .build(allAvailableQuests.stream().map(QuestButton::new).collect(Collectors.toList())));
    }
    
    private final class QuestButton implements UIComponent {

        private final Quest quest;

        private QuestButton(Quest quest) {
            this.quest = quest;
        }

        @Override
        public UIComponent build(Layout layout) {
            return new Sized(
                Size.staticSize(layout.getWidth(), 30),
                new Padding(
                    new Pad.Builder().all(5).build(),
                    new Tooltip.Builder()
                        .withText(quest.getTitle())
                        .withText(quest.getDescription())
                        .build(new FlatButton.Builder()
                            .withOnPress((button) -> { if (button == 0) AllAvailableQuests.this.onSelectQuest.accept(quest); })
                            .build(new Center(new Text.Builder(quest.getTitle()).withOverflowBehaviour(Text.OverflowBehaviour.ELLIPSIS))))
                )
            );
        }
    }
}
