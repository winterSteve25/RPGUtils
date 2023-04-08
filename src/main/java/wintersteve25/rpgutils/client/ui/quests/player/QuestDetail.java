package wintersteve25.rpgutils.client.ui.quests.player;

import com.github.wintersteve25.tau.components.*;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.utils.Pad;
import com.github.wintersteve25.tau.utils.Vector2i;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;

import java.util.stream.Collectors;

public class QuestDetail implements UIComponent {
    private final Quest quest;

    public QuestDetail(Quest quest) {
        this.quest = quest;
    }

    @Override
    public UIComponent build(Layout layout) {
        
        if (quest == null) {
            return new Container.Builder()
                .withChild(new Center(new Text.Builder("No Quest Selected")));
        }
        
        return new Container.Builder()
            .withChild(new Padding(
                new Pad.Builder().all(10).build(),
                new Column.Builder()
                    .withAlignment(LayoutSetting.START)
                    .build(
                        new Text.Builder(quest.getTitle()),
                        new Spacer(new Vector2i(0, 10)),
                        new Text.Builder(quest.getDescription()),
                        new Spacer(new Vector2i(0, 10)),
                        new Text.Builder("Objectives:"),
                        new Spacer(new Vector2i(0, 2)),
                        new Padding(
                            new Pad.Builder().left(10).build(),
                            new Column.Builder()
                                .withAlignment(LayoutSetting.START)
                                .build(quest.getObjectives().stream().map(ObjectiveDisplay::new).collect(Collectors.toList()))
                        )
                    )
            ));
    }
    
    private static class ObjectiveDisplay implements UIComponent {

        private final IObjective objective;

        private ObjectiveDisplay(IObjective objective) {
            this.objective = objective;
        }

        @Override
        public UIComponent build(Layout layout) {
            return new Row.Builder()
                    .withAlignment(LayoutSetting.START)
                    .build(new Text.Builder("- "), new Text.Builder(objective.objectiveTitle()));
        }
    }
}