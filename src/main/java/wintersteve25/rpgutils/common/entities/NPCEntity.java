package wintersteve25.rpgutils.common.entities;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.common.data.loaded.npc.datum_type.MapNPCDatumType;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.npc.datum_type.SoundEventNPCDatumType;
import wintersteve25.rpgutils.common.data.loaded.npc.datum_type.StringNPCDatumType;
import wintersteve25.rpgutils.common.data.loaded.npc.goal.ModGoals;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketSetType;
import wintersteve25.rpgutils.common.registry.ModMemoryModuleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NPCEntity extends MobEntity implements IAnimatedEntity<NPCEntity> {

    private final AnimationFactory factory = new AnimationFactory(this);
    /**
     * Use caution when accessing directly - invoking getNPCType() instead will ensure initialisation from NBT if necessary.
     */
    private NPCType npcType = null;

    public NPCEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static NPCEntity create(EntityType<? extends MobEntity> entityType, World world, String type) {
        NPCEntity entity = new NPCEntity(entityType, world);
        entity.setNPCType(type);
        return entity;
    }

    public static AttributeModifierMap createAttributes() {
        return NPCTypeLoader.INSTANCE.createDefaultAttributes();
    }

    @Override
    protected void registerGoals() {
        NPCType type = getNPCType();
        if (type != null) {
            Map<ModGoals.GoalConstructor, Integer> goalWeights = type.getDatum(MapNPCDatumType.GOALS);
            for (Map.Entry<ModGoals.GoalConstructor, Integer> goal : goalWeights.entrySet()) {
                this.goalSelector.addGoal(goal.getValue(), goal.getKey().apply(this));
            }
        }
    }

    @Override
    protected Brain.BrainCodec<NPCEntity> brainProvider() {
        return Brain.provider(ImmutableList.of(ModMemoryModuleTypes.MOVEMENT_TARGET.get()), ImmutableList.of(SensorType.NEAREST_PLAYERS));
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        Brain<NPCEntity> brain = this.brainProvider().makeBrain(dynamic);
        brain.setMemory(ModMemoryModuleTypes.MOVEMENT_TARGET.get(), new BlockPos(0, 61, 0));
        return brain;
    }

    /**
     * Sets the NPC's type. Should only be used once immediately after construction - otherwise duplicate goals will be added. Updates persistent data, attributes and goals.
     * @param type The name (as in NPCTypeLoader.typeMap) of the new NPC type
     */
    private void setNPCType(String type) {
        this.npcType = NPCTypeLoader.INSTANCE.getType(type);
        this.getPersistentData().putString(getTypeKey(), this.npcType.getDatum(StringNPCDatumType.NAME));
        NPCTypeLoader.INSTANCE.setAttributes(this, this.npcType.getDatum(StringNPCDatumType.NAME));
        this.registerGoals();
    }

    /**
     * Null check should always be performed on result - alternatively, use getTypeOrElse()
     * @return The type of the NPC
     */
    public NPCType getNPCType() {
        if (npcType == null) {
            String type = this.getPersistentData().getString(getTypeKey());
            if (!type.equals("")) {
                this.setNPCType(type);
            } else npcType = null;
        }
        return npcType;
    }

    private <T> T getTypeOrElse(Function<NPCType, T> ifValid, T ifNull) {
        NPCType type = getNPCType();
        if (type != null) {
            return ifValid.apply(type);
        }
        return ifNull;
    }

    /**
     * Sets the NPC's type. Should be used only by the client on receival of PacketSetType. Does not update persistent data or attributes.
     * @param type The new NPC type
     */
    public void setNPCType(NPCType type) {
        this.npcType = type;
    }

    public void updateClients() {
        for (ServerPlayerEntity player : getServer().getLevel(this.level.dimension()).players()) {
            updateClient(player);
        }
    }

    public void updateClient(ServerPlayerEntity client) {
        PacketSetType packet = new PacketSetType(this.getId(), getNPCType());
        ModNetworking.sendToClient(packet, client);
    }

    public String getTexturePath() {
        return getTypeOrElse(t -> t.getDatum(StringNPCDatumType.TEXTURE), NPCType.DEFAULT_TEXTURE);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return getTypeOrElse(t -> t.getDatum(SoundEventNPCDatumType.AMBIENT_SOUND), SoundEvents.VILLAGER_AMBIENT);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return getTypeOrElse(t -> t.getDatum(SoundEventNPCDatumType.HURT_SOUND), SoundEvents.VILLAGER_HURT);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return getTypeOrElse(t -> t.getDatum(SoundEventNPCDatumType.DEATH_SOUND), SoundEvents.VILLAGER_DEATH);
    }

    @Override
    public void setAnimation(String controller, String animation) {}

    @Override
    public NPCEntity getSelf() {
        return this;
    }

    private static String getTypeKey() {
        return RPGUtils.MOD_ID + "_name";
    }

    @Override
    public ITextComponent getName() {
        String name = getTypeOrElse(t -> t.getDatum(StringNPCDatumType.NAME), "npc");
        return new TranslationTextComponent("entity." + RPGUtils.MOD_ID + ".npc." + name);
    }
}
