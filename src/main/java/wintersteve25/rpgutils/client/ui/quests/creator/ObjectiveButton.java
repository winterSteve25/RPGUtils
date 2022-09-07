package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.ContextMenu;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.FinishDialogueObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveButton extends NordButton {

    private final ContextMenu contextMenu;
    private IObjective objective;
    
    public ObjectiveButton(Panel panel, ITextComponent txt, Icon icon, EditObjectivesPanel editObjectivesPanel) {
        super(panel, txt, icon);
        
        List<ContextMenuItem> options = new ArrayList<>();
        options.add(new ContextMenuItem(new StringTextComponent("Remove"), Icon.EMPTY, () -> editObjectivesPanel.remove(this)));
        contextMenu = new ContextMenu(parent, options);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (mouseButton.isLeft()) {
            
        } else if (mouseButton.isRight()) {
            playClickSound();
            openContextMenu(contextMenu);
        }
    }

    public IObjective getObjective() {
        return new FinishDialogueObjective(new ResourceLocation("hello"));
    }

    public void setObjective(IObjective objective) {
        this.objective = objective;
        if (objective == null) return;
        setIcon(objective.objectiveIcon());
        setTitle(objective.objectiveTitle());
    }
}
