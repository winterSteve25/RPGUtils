package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.item.minecart.CommandBlockMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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
import wintersteve25.rpgutils.common.data.loaded.npc.NPCAttributeLoader;

import javax.annotation.Nullable;

public class NPCEntity extends MobEntity implements IAnimatedEntity<NPCEntity> {

    private static final DataParameter<String> DATA_ID_TYPE = EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private final AnimationFactory factory = new AnimationFactory(this);
    private String npcType;

    public NPCEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap createAttributes() {
        return NPCAttributeLoader.INSTANCE.createDefaultAttributes();
    }

    protected void registerGoals() {
        // TODO: Custom goals here
        goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8.0f));
    }

    public void setType(String type) {
        this.npcType = type;
        this.getPersistentData().putString(getNameKey(), this.npcType);
        NPCAttributeLoader.INSTANCE.setAttributes(this, this.npcType);
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
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    public void setAnimation(String controller, String animation) {

    }

    @Override
    public NPCEntity getSelf() {
        return this;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString(getNameKey(), npcType);
        return nbt;
    }

    private static String getNameKey() {
        return RPGUtils.MOD_ID + "_name";
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        npcType = nbt.getString(getNameKey());
    }
}
