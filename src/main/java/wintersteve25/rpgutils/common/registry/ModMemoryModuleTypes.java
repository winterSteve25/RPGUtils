package wintersteve25.rpgutils.common.registry;

import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.RPGUtils;

import java.util.Optional;

public class ModMemoryModuleTypes {

    private static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, RPGUtils.MOD_ID);

    public static final RegistryObject<MemoryModuleType<BlockPos>> MOVEMENT_TARGET = REGISTER.register("quest_target", () -> new MemoryModuleType<>(Optional.ofNullable(BlockPos.CODEC)));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
