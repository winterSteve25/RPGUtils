package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketSetType;

import javax.annotation.Nullable;

public class NPCEntity extends MobEntity implements IAnimatedEntity<NPCEntity> {

    private final AnimationFactory factory = new AnimationFactory(this);
    private NPCType npcType;
    @OnlyIn(Dist.CLIENT)
    private String clientPath = NPCType.DEFAULT_TEXTURE;

    public NPCEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap createAttributes() {
        return NPCTypeLoader.INSTANCE.createDefaultAttributes();
    }

    protected void registerGoals() {
        // TODO: Custom goals here
        goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8.0f));
    }

    public void setNPCType(String type) {
        this.npcType = NPCTypeLoader.INSTANCE.getType(type);
        this.getPersistentData().putString(getNameKey(), this.npcType.getName());
        NPCTypeLoader.INSTANCE.setAttributes(this, this.npcType.getName());
        PacketSetType packet = new PacketSetType(this.getId(), npcType.getPath());
        for (ServerPlayerEntity player : getServer().getLevel(this.level.dimension()).players()) {
            ModNetworking.sendToClient(packet, player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setClientPath(String path) {
        this.clientPath = path;
    }

    @OnlyIn(Dist.CLIENT)
    public String getClientPath() {
        return clientPath;
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
        nbt.putString(getNameKey(), npcType.getName());
        return nbt;
    }

    private static String getNameKey() {
        return RPGUtils.MOD_ID + "_name";
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        npcType = NPCTypeLoader.INSTANCE.getType(nbt.getString(getNameKey()));
    }
}
