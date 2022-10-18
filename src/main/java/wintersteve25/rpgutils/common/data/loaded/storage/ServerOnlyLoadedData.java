package wintersteve25.rpgutils.common.data.loaded.storage;

import wintersteve25.rpgutils.common.data.loaded.DataLoader;
import wintersteve25.rpgutils.common.data.loaded.boss_room.BossRoomDataLoader;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;

import java.util.ArrayList;
import java.util.List;

public class ServerOnlyLoadedData {
    private static final List<DataLoader<?>> dataLoaders;

    private ServerOnlyLoadedData() {}
    
    static {
        dataLoaders = new ArrayList<>();
        dataLoaders.add(QuestsManager.INSTANCE);
        dataLoaders.add(NPCTypeLoader.INSTANCE);
        dataLoaders.add(BossRoomDataLoader.INSTANCE);
    }

    public static void reloadAll() {
        for (DataLoader<?> dataLoader : dataLoaders) {
            dataLoader.read();
        }
    }
}
