package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueParser;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.UnsupportedParameter;

import java.util.List;

public interface ParsedDialogueAction extends ParsedDialogueLine {
    DialogueParser PARAMETER_PARSER = new DialogueParser();
    
    default void addListEntry(String value) {
        throw new UnsupportedParameter("List");
    }

    default void addMapEntry(String name, String value) {
        throw new UnsupportedParameter("Map");
    }
    
    default void addNormalParameter(String parameter) {
        throw new UnsupportedParameter("Normal");
    }
    
    default void addEntryParameters(List<String> parameters) {
        throw new UnsupportedParameter("Entry Parameter");
    }
    
    default void parseParameters(List<String> parameters) {
        PARAMETER_PARSER.parseParameters(this, parameters);
    }
}