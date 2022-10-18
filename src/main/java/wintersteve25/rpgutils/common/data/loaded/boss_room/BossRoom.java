package wintersteve25.rpgutils.common.data.loaded.boss_room;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.utils.geom.Cuboid;
import wintersteve25.rpgutils.common.utils.geom.Vector;

public class BossRoom {

    private final String dimension;
    private final Cuboid room;
    private final String npcType;
    private final Cuboid door;
    private final Block doorMaterial;

    private boolean triggered = false;

    public BossRoom(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        int z = object.get("z").getAsInt();
        int width = object.get("width").getAsInt();
        int height = object.get("height").getAsInt();
        int depth = object.get("depth").getAsInt();
        room = new Cuboid(x, y, z, width, height, depth);
        npcType = object.get("npcType").getAsString();
        int doorX = object.get("doorX").getAsInt();
        int doorY = object.get("doorY").getAsInt();
        int doorZ = object.get("doorZ").getAsInt();
        int doorWidth = object.get("doorWidth").getAsInt();
        int doorHeight = object.get("doorHeight").getAsInt();
        int doorDepth = object.get("doorDepth").getAsInt();
        door = new Cuboid(doorX, doorY, doorZ, doorWidth, doorHeight, doorDepth);
        String blockKey = object.get("doorMaterial").getAsString();
        doorMaterial = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockKey));
        dimension = object.get("dimension").getAsString();
    }

    public void tick() {
        try {
            if (getLevel().isLoaded(room.getStart().asBlockPos()) && !triggered) {
                for (ServerPlayerEntity player : getLevel().players()) {
                    if (room.contains(new Vector(player.blockPosition()))) {
                        for (Vector vector : door) {
                            getLevel().setBlock(vector.asBlockPos(), doorMaterial.defaultBlockState(), 3);
                        }
                        triggered = true;
                        NPCEntity npcEntity = NPCEntity.create(getLevel(), npcType);
                        npcEntity.setPos(room.MID(Direction.Axis.X), room.getStart().get(1) + 1, room.MID(Direction.Axis.Z));
                        getLevel().addFreshEntity(npcEntity);
                        npcEntity.updateClients();
                        break;
                    }
                }
            }
        } catch (IllegalStateException e) {
            // Skip tick
        }
    }

    private ServerWorld getLevel() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ResourceLocation resourceLocation = new ResourceLocation(dimension);
        for (RegistryKey<World> registryKey : server.levelKeys()) {
            if (registryKey.location().equals(resourceLocation)) {
                return server.getLevel(registryKey);
            }
        }
        throw new IllegalStateException("No such level: " + resourceLocation);
    }
}
