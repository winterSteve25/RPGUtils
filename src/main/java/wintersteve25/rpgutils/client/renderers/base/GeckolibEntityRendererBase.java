package wintersteve25.rpgutils.client.renderers.base;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeckolibEntityRendererBase<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
    public GeckolibEntityRendererBase(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }
}
