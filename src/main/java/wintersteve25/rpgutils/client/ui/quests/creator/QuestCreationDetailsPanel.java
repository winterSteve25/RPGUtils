package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

public class QuestCreationDetailsPanel extends Panel {
    
    private final TextBox title;
    private final TextBox description;
    
    private Quest.Builder builder;
    private boolean enabled;

    public QuestCreationDetailsPanel(Panel panel) {
        super(panel);
        title = new TextBox(this) {
            @Override
            public void onTextChanged() {
                builder.setTitle(new StringTextComponent(title.getText()));
            }
        };
        title.setSize(100, 20);
        title.ghostText = "Title";
        description = new TextBox(this) {
            @Override
            public void onTextChanged() {
                builder.setDescription(new StringTextComponent(description.getText()));
            }
        };
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
        builder = button.builder;
        title.setText(builder.getTitle());
        description.setText(builder.getDescription());
        refreshWidgets();
    }
}