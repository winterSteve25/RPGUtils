package wintersteve25.rpgutils.common.data.loaded.storage;

import wintersteve25.rpgutils.common.data.loaded.DataLoader;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DialogueManager;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue_pool.DialoguePoolManager;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;

import java.util.ArrayList;
import java.util.List;

public class ClientOnlyLoadedData {
    private static final List<DataLoader<?>> dataLoaders;
    
    private ClientOnlyLoadedData() {
    }
    
    static {
        dataLoaders = new ArrayList<>();
        dataLoaders.add(QuestsManager.INSTANCE);
        dataLoaders.add(DialogueManager.INSTANCE);
        dataLoaders.add(DialoguePoolManager.INSTANCE);
        dataLoaders.add(NPCTypeLoader.INSTANCE);
    }
    
    public static void reloadAll() {
        for (DataLoader<?> dataLoader : dataLoaders) {
            dataLoader.read();
        }
    }
}
