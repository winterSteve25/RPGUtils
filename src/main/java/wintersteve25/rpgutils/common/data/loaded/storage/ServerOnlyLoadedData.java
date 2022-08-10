package wintersteve25.rpgutils.common.data.loaded.storage;

import wintersteve25.rpgutils.common.data.loaded.DataLoader;
import wintersteve25.rpgutils.common.data.loaded.quest.QuestsManager;

import java.util.ArrayList;
import java.util.List;

public class ServerOnlyLoadedData {
    private static List<DataLoader<?>> dataLoaders;

    private ServerOnlyLoadedData() {
    }
    
    static {
        dataLoaders = new ArrayList<>();
        dataLoaders.add(QuestsManager.INSTANCE);
    }

    public static void reloadAll() {
        for (DataLoader<?> dataLoader : dataLoaders) {
            dataLoader.read();
        }
    }
}
