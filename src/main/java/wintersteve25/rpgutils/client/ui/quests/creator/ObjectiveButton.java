package wintersteve25.rpgutils.client.ui.quests.creator;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
            objective.openEditObjectiveMenu(obj -> {
                setObjective(obj);
                getGui().openGui();
            }, () -> getGui().openGui());
        } else if (mouseButton.isRight()) {
            playClickSound();
            openContextMenu(contextMenu);
        }
    }

    @Override
    public void draw(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        super.draw(matrixStack, theme, x, y, w, h);
    }

    public IObjective getObjective() {
        return objective;
    }

    public void setObjective(IObjective objective) {
        this.objective = objective;
        if (objective == null) return;
        setIcon(objective.objectiveIcon());
        setTitle(objective.objectiveTitle());
    }
}
