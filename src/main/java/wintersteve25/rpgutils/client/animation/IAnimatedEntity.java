package wintersteve25.rpgutils.client.animation;

import net.minecraft.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public interface IAnimatedEntity<T extends LivingEntity> extends IAnimatable {
    void setAnimation(String controller, String animation);

    T getSelf();

    static <T1 extends LivingEntity> IAnimatedEntity<T1> nonAnimated(T1 entity) {
        return new IAnimatedEntity<T1>() {
            @Override
            public void setAnimation(String controller, String animation) {
            }

            @Override
            public T1 getSelf() {
                return entity;
            }

            @Override
            public void registerControllers(AnimationData data) {
            }

            @Override
            public AnimationFactory getFactory() {
                return null;
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    static <T1 extends LivingEntity> IAnimatedEntity<T1> getOrCreate(T1 entity) {
        if (entity instanceof IAnimatedEntity<?>) {
            return (IAnimatedEntity<T1>) entity;
        }
        
        return nonAnimated(entity);
    }
}