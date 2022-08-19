package wintersteve25.rpgutils.client.ui.dialogues.selections.npc_id;

import wintersteve25.rpgutils.client.ui.components.selection.SelectionOption;

public class NpcIDOption extends SelectionOption<NpcIDOption> {
    private final String npcID;
    
    public NpcIDOption(int x, int y, SelectNpcID parent, int index, String npcID) {
        super(x, y, npcID, parent, index);
        this.npcID = npcID;
    }
    
    public NpcIDOption(NpcIDOption copyFrom) {
        super(copyFrom);
        this.npcID = copyFrom.npcID;
    }

    public String getNpcID() {
        return npcID;
    }
}