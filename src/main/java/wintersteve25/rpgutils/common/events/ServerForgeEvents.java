package wintersteve25.rpgutils.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.capabilities.base.CapabilityProvider;
import wintersteve25.rpgutils.common.data.loaded.quest.PlayerQuestProgress;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractBlockTrigger;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractEntityTrigger;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.registry.ModCapabilities;
import wintersteve25.rpgutils.common.registry.ModCommands;
import wintersteve25.rpgutils.common.registry.ModEntities;

@Mod.EventBusSubscriber(modid = RPGUtils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerForgeEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher());
    }
    
    @SubscribeEvent
    public static void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ServerOnlyLoadedData.reloadAll();
    }
    
    @SubscribeEvent
    public static void playerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.level;
        if (world instanceof ServerWorld) {
            for (Entity entity : ((ServerWorld) world).getEntities(ModEntities.NPC_ENTITY.get(), (e) -> true)) {
                if (entity instanceof NPCEntity) {
                    ((NPCEntity) entity).updateClient((ServerPlayerEntity) player);
                }
            }
            
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NpcIDMapping.refreshClient(serverPlayerEntity);
            PlayerQuestProgress.refreshClient(serverPlayerEntity);
        }
    }
    
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            if (!entity.getCapability(ModCapabilities.PLAYER_QUEST).isPresent()) {
                CapabilityProvider<PlayerQuestProgress> provider = new CapabilityProvider<>(new PlayerQuestProgress(), ModCapabilities.PLAYER_QUEST);
                event.addCapability(new ResourceLocation(RPGUtils.MOD_ID, "player_quest"), provider);
                event.addListener(provider::invalidate);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();
        if (world.isClientSide()) return;
        if (player instanceof FakePlayer) return;
        player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
            if (cap.getCurrentQuest() == null) return;
            cap.trigger((ServerPlayerEntity) player, new InteractEntityTrigger(event.getTarget(), (ServerPlayerEntity) player));
        });
    }

    @SubscribeEvent
    public static void onPlayerInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();
        if (world.isClientSide()) return;
        if (player instanceof FakePlayer) return;
        player.getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
            if (cap.getCurrentQuest() == null) return;
            cap.trigger((ServerPlayerEntity) player, new InteractBlockTrigger(event.getPos(), (ServerWorld) world));
        });
    }
}