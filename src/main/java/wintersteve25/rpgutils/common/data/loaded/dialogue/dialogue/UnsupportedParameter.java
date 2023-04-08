package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

public class UnsupportedParameter extends RuntimeException {
    public UnsupportedParameter(String type) {
        super(type + " parameters are not supported!");
    }
}
