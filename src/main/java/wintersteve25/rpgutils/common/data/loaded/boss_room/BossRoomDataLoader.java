package wintersteve25.rpgutils.common.data.loaded.boss_room;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.JsonDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BossRoomDataLoader extends JsonDataLoader {

    public static final BossRoomDataLoader INSTANCE = new BossRoomDataLoader();

    private static final List<BossRoom> bossRooms = new ArrayList<>();

    private BossRoomDataLoader() {
        super("boss_room");
    }

    public static void tick() {
        bossRooms.forEach(BossRoom::tick);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : data.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            String path = resourceLocation.getPath();
            if (path.startsWith("_")) {
                continue; // Forge: filter anything beginning with "_" as it's used for metadata.
            }
            try {
                JsonObject root = entry.getValue().getAsJsonObject();
                bossRooms.add(new BossRoom(root));
            } catch (IllegalArgumentException | JsonParseException e) {
                RPGUtils.LOGGER.error("Parsing error loading NPC attribute set {}", resourceLocation, e);
            }
        }
    }
}
