package wintersteve25.rpgutils.common.events;

import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.creative.cmdcam.client.CMDCamClient;
import team.creative.cmdcam.server.CMDCamServer;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.camera_paths.CameraPathManager;
import wintersteve25.rpgutils.common.data.temporary.EntityIDMapper;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    
    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide()) {
            EntityIDMapper.CLIENT.data.put(event.getEntity().getUUID(), event.getEntity().getId());
        }
    }
    
    @SubscribeEvent
    public static void saveWorld(WorldEvent.Save event) {
        if (CMDCamClient.isInstalledOnSever) {
            World world = (World) event.getWorld();
            for (String path : CMDCamServer.getSavedPaths(world)) {
                CameraPathManager.INSTANCE.paths.put(path, CMDCamServer.getPath(world, path));
            }
        } else {
            CameraPathManager.INSTANCE.addAll(CMDCamClient.savedPaths);
        }
        
        CameraPathManager.INSTANCE.save();
    }
}
