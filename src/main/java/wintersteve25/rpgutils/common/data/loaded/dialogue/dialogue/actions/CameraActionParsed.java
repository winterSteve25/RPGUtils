package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import net.minecraft.util.math.vector.Vector3d;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

public class CameraActionParsed implements ParsedDialogueAction {

    private Vector3d face;
    
    public CameraActionParsed() {
    }

    @Override
    public void addMapEntry(String name, String value) {
        if (name.equals("face")) {
            String[] values = value.split(",");
            double x = Double.parseDouble(values[0].trim());
            double y = Double.parseDouble(values[1].trim());
            double z = Double.parseDouble(values[2].trim());
            face = new Vector3d(x, y, z);
        }
    }
    
    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return null;
    }
}
