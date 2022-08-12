package wintersteve25.rpgutils.common.data.loaded;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class DataLoader<T> {
    
    private final Path directory;
    private final String subdirectory;
    private final String fileExtension;

    protected DataLoader(String subdirectory, String fileExtension) {
        this.subdirectory = subdirectory;
        this.directory = FMLPaths.getOrCreateGameRelativePath(Paths.get(JsonUtilities.rpgutilsPath + "/" + subdirectory + "/"), "");
        this.fileExtension = fileExtension;
    }

    public final void read() {
        Collection<File> files = FileUtils.listFiles(directory.toFile(), new String[] {fileExtension}, true);
        Map<ResourceLocation, T> data = new HashMap<>();
        
        if (!files.isEmpty()) {
            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }

                try {
                    StringBuilder contentBuilder = new StringBuilder();
                    for (String line : Files.readAllLines(file.toPath())) {
                        contentBuilder.append(line).append("\n");
                    }

                    String s = file.getPath();
                    int i = s.indexOf(subdirectory) + subdirectory.length() + 1;
                    ResourceLocation rl = new ResourceLocation(RPGUtils.MOD_ID, s.substring(i, s.length() - (fileExtension.length() + 1)));

                    data.put(rl, map(contentBuilder.toString()));
                } catch (IOException ignore) {
                }
            }
        }
        
        apply(data);
    }
    
    protected abstract void apply(Map<ResourceLocation, T> data);
    
    protected abstract T map(String stringContent);
}
