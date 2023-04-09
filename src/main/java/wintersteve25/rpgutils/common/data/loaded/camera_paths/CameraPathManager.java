package wintersteve25.rpgutils.common.data.loaded.camera_paths;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import team.creative.cmdcam.common.util.CamPath;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CameraPathManager {
    public static final CameraPathManager INSTANCE = new CameraPathManager();

    public final Map<String, CamPath> paths = new HashMap<>();
    private final File directory;
    private final String subdirectory;
    
    private CameraPathManager() {
        this.subdirectory = "camera_paths";
        this.directory = FMLPaths.getOrCreateGameRelativePath(Paths.get(JsonUtilities.rpgutilsPath + "/" + subdirectory + "/"), "").toFile();
    }
    
    public void addAll(Map<String, CamPath> paths) {
        this.paths.putAll(paths);
    }

    public void save() {
        RPGUtils.LOGGER.info("Saving camera paths");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                RPGUtils.LOGGER.warn("Failed to create Camera Paths directory");
            }
        }

        for (Map.Entry<String, CamPath> path : paths.entrySet()) {
            File file = new File(directory, path.getKey() + ".nbt");
            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                CompressedStreamTools.writeCompressed(path.getValue().writeToNBT(new CompoundNBT()), fileOutputStream);
            } catch (IOException e) {
                RPGUtils.LOGGER.warn("Failed to write path {} to disk", path.getKey());
            }
        }
    }

    public void load() {
        RPGUtils.LOGGER.info("Loading camera paths");
        Collection<File> files = FileUtils.listFiles(directory, new String[] {"nbt"}, true);

        if (!files.isEmpty()) {
            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }

                try {
                    CamPath path = new CamPath(CompressedStreamTools.readCompressed(file));
                    String s = file.getPath();
                    int i = s.indexOf(subdirectory) + subdirectory.length() + 1;
                    paths.put(s.substring(i, s.lastIndexOf(".")), path);
                } catch (IOException exception) {
                    RPGUtils.LOGGER.warn("Failed to read path: {}", file.getPath());
                    exception.printStackTrace();
                }
            }
        }
    }
}
