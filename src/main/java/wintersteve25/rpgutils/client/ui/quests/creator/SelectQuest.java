package wintersteve25.rpgutils.client.ui.quests.creator;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.SelectionOption;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectQuest extends AbstractSelectionUI<SelectQuest.SelectQuestOption> {

    private final ResourceLocation exclude;
    
    public SelectQuest(Consumer<List<SelectQuestOption>> onSubmit, ResourceLocation exclude) {
        super(true, onSubmit);
        this.exclude = exclude;
    }

    @Override
    protected void populateOptions(List<SelectQuestOption> list) {
        List<Quest> quests = new ArrayList<>(QuestsManager.INSTANCE.getQuests().values());

        boolean skipped = false;
        
        for (int i = 0; i < quests.size(); i++) {
            Quest q = quests.get(i);
            if (q.getResourceLocation().equals(exclude)) {
                skipped = true;
                continue;
            }
            list.add(new SelectQuestOption(this.x + 10, this.y + 40 + (skipped ? i - 1 : i) * 12, q, this, (skipped ? i - 1 : i)));
        }
    }

    @Override
    protected boolean atLeastOne() {
        return false;
    }

    @Override
    protected SelectQuestOption copyFrom(SelectQuestOption copyFrom) {
        return new SelectQuestOption(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof SelectQuestOption;
    }

    public static class SelectQuestOption extends SelectionOption<SelectQuestOption> {
        private final Quest quest;

        public SelectQuestOption(int x, int y, Quest quest, AbstractSelectionUI<SelectQuestOption> parent, int index) {
            super(x, y, quest.getResourceLocation().toString(), parent, index);
            this.quest = quest;
        }

        public SelectQuestOption(SelectQuestOption copyFrom) {
            super(copyFrom);
            this.quest = copyFrom.quest;
        }

        public Quest getQuest() {
            return quest;
        }
    }
}