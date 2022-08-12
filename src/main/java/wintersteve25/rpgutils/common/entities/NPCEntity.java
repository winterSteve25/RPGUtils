package wintersteve25.rpgutils.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.common.utils.DataUtils;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;

public class NPCEntity extends MobEntity implements IAnimatedEntity<NPCEntity> {
    private static final DataParameter<ResourceLocation> MODEL_LOC = EntityDataManager.defineId(NPCEntity.class, DataUtils.RESOURCE_LOCATION_SERIALIZER);
    private static final DataParameter<ResourceLocation> ANIM_LOC = EntityDataManager.defineId(NPCEntity.class, DataUtils.RESOURCE_LOCATION_SERIALIZER);
    private static final DataParameter<ResourceLocation> TEXTURE_LOC = EntityDataManager.defineId(NPCEntity.class, DataUtils.RESOURCE_LOCATION_SERIALIZER);
    
    private final AnimationFactory factory = new AnimationFactory(this);

    public NPCEntity(EntityType<? extends NPCEntity> type, World world) {
        super(type, world);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType pSlot, ItemStack pStack) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.LEFT;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MODEL_LOC, RLHelper.geoModel("npc_default"));
        this.entityData.define(ANIM_LOC, RLHelper.geoAnim("npc_default"));
        this.entityData.define(TEXTURE_LOC, RLHelper.texture("npc_default"));
    }

    @Override
    protected void registerGoals() {
        
    }

    public ResourceLocation getModelLocation() {
        return this.entityData.get(MODEL_LOC);
    }

    public ResourceLocation getAnimationLocation() {
        return this.entityData.get(ANIM_LOC);
    }

    public ResourceLocation getTextureLocation() {
        return this.entityData.get(TEXTURE_LOC);
    }
    
    public void setModelLocation(ResourceLocation location) {
        this.entityData.set(MODEL_LOC, location);
    }
    
    public void setAnimationLocation(ResourceLocation location) {
        this.entityData.set(ANIM_LOC, location);
    }
    
    public void setTextureLocation(ResourceLocation location) {
        this.entityData.set(TEXTURE_LOC, location);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("model_loc", getModelLocation().toString());
        pCompound.putString("anim_loc", getAnimationLocation().toString());
        pCompound.putString("texture_loc", getTextureLocation().toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("model_loc")) {
            setModelLocation(new ResourceLocation(pCompound.getString("model_loc")));
            setAnimationLocation(new ResourceLocation(pCompound.getString("anim_loc")));
            setTextureLocation(new ResourceLocation(pCompound.getString("texture_loc")));
        }
    }
    
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc_default.idle", true));
        return PlayState.CONTINUE;
    }
    
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void setAnimation(String controller, String animation) {
        
    }

    @Override
    public NPCEntity getSelf() {
        return this;
    }

    public static AttributeModifierMap.MutableAttribute createAttribute() {
        return LivingEntity.createLivingAttributes();
    }
}