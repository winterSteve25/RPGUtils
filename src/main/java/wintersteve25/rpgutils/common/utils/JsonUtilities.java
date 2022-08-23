package wintersteve25.rpgutils.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import wintersteve25.rpgutils.common.data.loaded.storage.ClientOnlyLoadedData;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketLoadData;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Function;

public class JsonUtilities {
    public static String getOrDefault(JsonObject object, String key, String defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsString();
    }

    public static int getOrDefault(JsonObject object, String key, int defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsInt();
    }

    public static float getOrDefault(JsonObject object, String key, float defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsFloat();
    }

    public static boolean getOrDefault(JsonObject object, String key, boolean defaultValue) {
        if (!object.has(key)) return defaultValue;
        return object.get(key).getAsBoolean();
    }
    
    public static <T> T getOrDefault(JsonObject object, String key, Function<JsonElement, T> mapper, T defaultValue) {
        if (!object.has(key)) return defaultValue;
        return mapper.apply(object.get(key));
    }
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Path rpgutilsPath = FMLPaths.getOrCreateGameRelativePath(Paths.get("rpgutils/"), "");

    public static void saveDialogue(ResourceLocation resourceLocation, Object jsonObject) {
        saveData(resourceLocation, jsonObject, "/dialogues/");
    }

    private static void saveData(ResourceLocation resourceLocation, Object jsonObject, String subdirectory) {
        try {
            FileWriter fileWriter = new FileWriter(getGeneratedPath(resourceLocation, subdirectory));
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClientOnlyLoadedData.reloadAll();
    }

    public static void saveDialoguePool(ResourceLocation resourceLocation, Object jsonObject) {
        saveData(resourceLocation, jsonObject, "/dialogue_pools/");
    }

    public static void saveNpcAttributes(ResourceLocation resourceLocation, Object jsonObject) {
        saveData(resourceLocation, jsonObject, "/npc/attributes");
    }

    public static void saveQuest(ResourceLocation resourceLocation, Object jsonObject) {
        saveData(resourceLocation, jsonObject, "/quests/");
        ModNetworking.sendToServer(new PacketLoadData());
    }

    public static void deleteDialogue(ResourceLocation resourceLocation) {
        File file = new File(getGeneratedPath(resourceLocation, "/dialogues/"));
        file.delete();
    }

    public static void deleteAllFiles(String path) {
        Path directory = FMLPaths.getOrCreateGameRelativePath(Paths.get(rpgutilsPath + "/" + path + "/"), "");
        Collection<File> files = FileUtils.listFiles(directory.toFile(), null, true);
        for (File file : files) {
            file.delete();
        }
    }
    
    private static String getGeneratedPath(ResourceLocation resourceLocation, String subdirectory) {
        String rlPath = resourceLocation.getPath();
        int lastDir = rlPath.lastIndexOf('/');
        String path;

        Path path1 = Paths.get(rpgutilsPath + subdirectory);
        if (lastDir == -1) path = FMLPaths.getOrCreateGameRelativePath(path1, "").toString() + "/" + rlPath + ".json";
        else path = FMLPaths.getOrCreateGameRelativePath(Paths.get(path1 + "/" + rlPath.substring(0, lastDir)), "") + rlPath.substring(lastDir) + ".json";
        
        return path;
    }
}
