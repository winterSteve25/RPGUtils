package wintersteve25.rpgutils.common.registry;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class ModEntities {
    private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, RPGUtils.MOD_ID);

    public static final RegistryObject<EntityType<NPCEntity>> NPC_ENTITY = REGISTER.register("npc", () -> EntityType.Builder
            .of(NPCEntity::new, EntityClassification.MISC)
            .sized(0.6f, 1.95f)
            .build(new ResourceLocation(RPGUtils.MOD_ID, "npc").toString()));
    
    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    } 
}
