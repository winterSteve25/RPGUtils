package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;

public class CameraAction implements RuntimeDialogueAction {
    
    private final Vector3d face;
    private final DialogueContext context;
    
    public CameraAction(Vector3d face, DialogueContext context) {
        this.face = face;
        this.context = context;
    }

    @Override
    public void execute(DialogueUI ui) {
        
    }

    @Override
    public boolean tick(DialogueUI ui, float progress) {
        
        return false;
    }

    @Override
    public boolean isComplete(DialogueUI ui) {
        return cameraEntity;
    }
}
