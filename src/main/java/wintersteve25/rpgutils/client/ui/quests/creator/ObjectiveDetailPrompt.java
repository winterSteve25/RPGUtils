package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.SelectTypePanel;
import wintersteve25.rpgutils.client.ui.components.SelectableType;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjectiveType;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.ObjectiveTypes;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ObjectiveDetailPrompt extends BaseScreen {

    private boolean enabled;
    private ObjectiveButton objectiveButton;
    private Consumer<ObjectiveButton> onSubmit;
    
    private final ObjectiveTypePanel objectiveTypePanel;
    private final SubmitOrCancel submitOrCancel;

    public ObjectiveDetailPrompt(Panel panel) {
        parent = panel;
        setSize(240, 200);
        objectiveTypePanel = new ObjectiveTypePanel(this);
        submitOrCancel = new SubmitOrCancel(this, () -> {
            if (objectiveTypePanel.objective == null) return;
            objectiveButton.setObjective(objectiveTypePanel.objective);
            onSubmit.accept(objectiveButton);
            disable();
        }, this::disable);
        submitOrCancel.setSize(105, 20);
    }
    
    @Override
    public void addWidgets() {
        if (!enabled) return;
        TextField title = new TextField(this);    
        title.setText("Create Objective");
        add(title);
        add(objectiveTypePanel);
        add(submitOrCancel);
    }

    @Override
    public void alignWidgets() {
        objectiveTypePanel.initGui();
        align(new CenterLayout(10));
    }

    @Override
    public boolean shouldDraw() {
        return enabled;
    }

    public void disable() {
        enabled = false;
        objectiveButton = null;
        initGui();
    }

    public void enable(ObjectiveButton objectiveButton, Consumer<ObjectiveButton> onSubmit) {
        enabled = true;
        this.objectiveButton = objectiveButton;
        this.onSubmit = onSubmit;
        objectiveTypePanel.setObjective(objectiveButton.getObjective());
        initGui();
    }

    private static class ObjectiveTypePanel extends SelectTypePanel<IObjectiveType<?>> {
        
        private IObjective objective;
        
        public ObjectiveTypePanel(Panel panel) {
            super(panel);
            
            setSelectableTypes(ObjectiveTypes.TYPES.values().stream().sorted((a, b) -> a.name().getContents().compareToIgnoreCase(b.name().getContents())).map(objType -> new SelectableType<IObjectiveType<?>>(Icon.EMPTY, objType.name(), (btn, mouse) -> {
                if (mouse.isLeft()) {
                    playClickSound();
                    objType.openConfigScreen(obj -> {
                        panel.parent.openGui();
                        objective = obj;
                    }, () -> panel.parent.openGui());
                }
            }, objType)).collect(Collectors.toList()));
        }

        public void setObjective(IObjective objective) {
            this.objective = objective;
        }
    }
}
