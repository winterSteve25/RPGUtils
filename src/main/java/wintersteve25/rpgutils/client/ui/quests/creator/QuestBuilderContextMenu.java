package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.ContextMenu;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class QuestBuilderContextMenu extends ContextMenu {
    public QuestBuilderContextMenu(Panel panel, List<ContextMenuItem> i) {
        super(panel, i);
    }
    
    public static QuestBuilderContextMenu create(Panel panel, AllQuestsPanel allQuestsPanel, QuestBuilderButton button) {
        List<ContextMenuItem> items = new ArrayList<>();

        items.add(new ContextMenuItem(new StringTextComponent("Rename"), Icon.EMPTY, () -> {
                
        }));
        
        items.add(new ContextMenuItem(new StringTextComponent("Remove"), Icon.EMPTY, () -> allQuestsPanel.remove(button)));
        
        return new QuestBuilderContextMenu(panel, items);
    }
}
