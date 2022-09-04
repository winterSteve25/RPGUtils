package wintersteve25.rpgutils.common.data.loaded.npc.property;

public class NoSuchGroupException extends RuntimeException {

    public NoSuchGroupException(String group) {
        super("No such property group: " + group);
    }
}
