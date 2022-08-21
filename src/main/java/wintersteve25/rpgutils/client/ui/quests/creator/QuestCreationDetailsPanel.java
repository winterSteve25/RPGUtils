package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;

public class QuestCreationDetailsPanel extends Panel {

    private boolean enabled;
    
    private final TextBox title;
    private final TextBox description;
    
    public QuestCreationDetailsPanel(Panel panel) {
        super(panel);
        title = new TextBox(this);
        title.setSize(100, 20);
        title.ghostText = "Title";
        description = new TextBox(this);
        description.setSize(100, 20);
        description.ghostText = "Description";
    }

    @Override
    public void addWidgets() {
        if (!enabled) {
            TextField titleText = new TextField(this);
            titleText.setText("You haven't selected a quest to edit");
            add(titleText);
            return;
        }
        
        TextField titleText = new TextField(this);
        titleText.setText("Quest Details");
        add(titleText);
        add(title);
        add(description);
    }
    
    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Vertical(0, 10, 0));
    }
    
    public void enable(QuestBuilderButton button) {
        enabled = true;
        refreshWidgets();
    }
}