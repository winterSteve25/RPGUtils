package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import team.creative.cmdcam.client.CMDCamClient;
import team.creative.cmdcam.client.PathParseException;
import team.creative.cmdcam.common.util.CamPath;
import team.creative.cmdcam.common.util.CamTarget;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueContext;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;

public class CameraAction implements RuntimeDialogueAction {

    private final CamTarget target;
    private final CamPath path;
    private final DialogueContext context;
    
    private boolean errored;
    
    public CameraAction(CamPath path, CamTarget target, DialogueContext context) {
        this.path = path;
        this.target = target;
        this.context = context;
    }

    @Override
    public void execute(DialogueUI ui) {
        if (path != null) {
            try {
                CMDCamClient.startPath(path);
            } catch (PathParseException e) {
                RPGUtils.LOGGER.error("Failed to start path: {}", e.toString());
                errored = true;
            }
        }
    }

    @Override
    public boolean isComplete(DialogueUI ui) {
        return path.hasFinished() || errored;
    }
}
