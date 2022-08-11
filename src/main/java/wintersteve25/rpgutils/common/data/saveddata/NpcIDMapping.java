package wintersteve25.rpgutils.common.data.saveddata;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.npcid.PacketAddNpcIDMapping;
import wintersteve25.rpgutils.common.network.npcid.PacketRemoveNpcIDMapping;
import wintersteve25.rpgutils.common.network.npcid.PacketSyncNpcIDMapping;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcIDMapping extends WorldSavedData {
    
    public static final NpcIDMapping clientInstance = new NpcIDMapping(true);
    private static final String NAME = "npc_id";
    
    private final boolean isClient;
    private final Map<String, UUID> mapping;
    
    public NpcIDMapping(boolean isClient) {
        super(NAME);
        this.isClient = isClient;
        mapping = new HashMap<>();
    }

    @Override
    public void load(CompoundNBT pCompound) {
        ListNBT keys = pCompound.getList("keys", Constants.NBT.TAG_STRING);
        ListNBT values = pCompound.getList("values", Constants.NBT.TAG_INT_ARRAY);
    
        for (int i = 0; i < keys.size(); i++) {
            mapping.put(keys.getString(i), NBTUtil.loadUUID(values.get(i)));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {

        ListNBT keys = new ListNBT();
        ListNBT values = new ListNBT();
        
        for (Map.Entry<String, UUID> entry : mapping.entrySet()) {
            keys.add(StringNBT.valueOf(entry.getKey()));
            values.add(NBTUtil.createUUID(entry.getValue()));
        }
        
        pCompound.put("keys", keys);
        pCompound.put("values", values);
        return pCompound;
    }

    /**
     * @param npcID the Npc ID
     * @param uuid the UUID assigned to the Npc ID
     * @param world should be supplied when called from the server, used to sync all clients. Use null when called from client
     *              
     * Maps a UUID to a Npc ID. Replaces the old one if the Npc ID already exists.
     */
    public void addMapping(String npcID, UUID uuid, @Nullable ServerWorld world) {
        addMapping(npcID, uuid, true, world);
    }

    // called internally used to sync client and server, do not call.
    public void addMapping(String npcID, UUID uuid, boolean updateOtherSide, @Nullable ServerWorld world) {
        mapping.remove(npcID);
        mapping.put(npcID, uuid);

        if (isClient) {
            if (!updateOtherSide) return;
            ModNetworking.sendToServer(new PacketAddNpcIDMapping(npcID, uuid));
        } else {
            setDirty();
            if (!updateOtherSide) return;
            for (ServerPlayerEntity player : world.players()) {
                ModNetworking.sendToClient(new PacketAddNpcIDMapping(npcID, uuid), player);
            }
        }
    }

    /**
     * @param npcID the Npc ID to remove
     * @param world should be supplied when called from the server, used to sync all clients. Use null when called from client
     *
     * Removes the uuid mapped to a Npc ID
     */
    public void removeMapping(String npcID, @Nullable ServerWorld world) {
        removeMapping(npcID, true, world);
    }

    // called internally used to sync client and server, do not call.
    public void removeMapping(String npcID, boolean updateOtherSide, @Nullable ServerWorld world) {
        mapping.remove(npcID);

        if (isClient) {
            if (!updateOtherSide) return;
            ModNetworking.sendToServer(new PacketRemoveNpcIDMapping(npcID));
        } else {
            setDirty();
            if (!updateOtherSide) return;
            for (ServerPlayerEntity player : world.players()) {
                ModNetworking.sendToClient(new PacketRemoveNpcIDMapping(npcID), player);
            }
        }
    }
    
    public UUID get(String npcID) {
        return mapping.get(npcID);
    }
    
    public boolean has(String npcID) {
        return mapping.containsKey(npcID);
    }
    
    public static void refreshClient(ServerPlayerEntity player) {
        ModNetworking.sendToClient(new PacketSyncNpcIDMapping(get(player.getCommandSenderWorld()).save(new CompoundNBT())), player);
    }

    public static NpcIDMapping get(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            return clientInstance;
        }

        if (worldIn.getServer() == null) {
            throw new RuntimeException("Server does not exist. This should not happens");
        }

        ServerWorld world = worldIn.getServer().getLevel(World.OVERWORLD);

        if (world == null) {
            throw new RuntimeException("Overworld does not exist. This should not happens");
        }

        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(() -> new NpcIDMapping(false), NAME);
    }
}
