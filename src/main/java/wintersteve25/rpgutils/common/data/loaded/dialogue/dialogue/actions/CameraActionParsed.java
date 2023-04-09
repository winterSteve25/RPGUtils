package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions;

import team.creative.cmdcam.common.util.CamPath;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.CameraAction;
import wintersteve25.rpgutils.common.data.loaded.camera_paths.CameraPathManager;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.client.ui.dialogues.runtime.RuntimeDialogueAction;

public class CameraActionParsed implements ParsedDialogueAction {

    private CamPath path;
    
    public CameraActionParsed() {
    }

    @Override
    public void addNormalParameter(String parameter) {
        if (!CameraPathManager.INSTANCE.paths.containsKey(parameter)) {
            RPGUtils.LOGGER.error("No camera path with id: `{}` is found!", parameter);
        }
        path = CameraPathManager.INSTANCE.paths.get(parameter);
    }

    @Override
    public void addMapEntry(String name, String value) {
        if (name.equals("path")) {
            if (!CameraPathManager.INSTANCE.paths.containsKey(value)) {
                RPGUtils.LOGGER.error("No camera path with id: `{}` is found!", value);
            }
            path = CameraPathManager.INSTANCE.paths.get(value);
        }
    }
    
    @Override
    public RuntimeDialogueAction createRuntime(DialogueContext context) {
        return new CameraAction(path, null, context);
    }
}