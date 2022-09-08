package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.client.ui.components.TypeSelector;
import wintersteve25.rpgutils.client.ui.components.TypeSelectorButton;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjective;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.IObjectiveType;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.ObjectiveTypes;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObjectiveDetailPrompt extends BaseScreen {

    private boolean enabled;
    private ObjectiveButton objectiveButton;
    private Consumer<ObjectiveButton> onSubmit;
    
    private final ObjectiveTypePanel objectiveTypePanel;
    private final SubmitOrCancel submitOrCancel;

    public ObjectiveDetailPrompt(Panel panel) {
        parent = panel;
        setSize(176, 100);
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
        objectiveTypePanel.setSelectedIndex(0);
        objectiveTypePanel.setObjective(objectiveButton.getObjective());
        initGui();
    }

    private static class ObjectiveTypePanel extends TypeSelector {
        
        private IObjective objective;
        
        public ObjectiveTypePanel(Panel panel) {
            super(panel, new ArrayList<>());
            setSize(110, 20);
            
            for (IObjectiveType<?> type : ObjectiveTypes.TYPES.values()) {
                TypeSelectorButton item = new TypeSelectorButton(Icon.EMPTY, type.name(), (btn, mouseButton) -> {
                    if (mouseButton.isLeft()) {
                        playClickSound();
                        type.openConfigScreen(obj -> {
                            panel.parent.openGui();
                            setObjective(obj);
                        }, () -> panel.parent.openGui());
                    }
                });
                options.add(item);
            }

            updateType();
        }

        public void setObjective(IObjective objective) {
            this.objective = objective;
        }
    }
}
