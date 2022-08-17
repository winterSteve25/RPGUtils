package wintersteve25.rpgutils.client.renderers;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import wintersteve25.rpgutils.RPGUtils;

public class GeckolibModelBase<T extends IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation path;
    private final ResourceLocation texture;
    private final ResourceLocation animation;

    public GeckolibModelBase(String pathModel, String pathTexture, String pathAnimation) {
        this.path = new ResourceLocation(RPGUtils.MOD_ID, pathModel);
        this.texture = new ResourceLocation(RPGUtils.MOD_ID, pathTexture);
        this.animation = new ResourceLocation(RPGUtils.MOD_ID, pathAnimation);
    }

    public GeckolibModelBase(GeckolibModelBase<?> other) {
        this.path = other.path;
        this.texture = other.texture;
        this.animation = other.animation;
    }
    
    @Override
    public ResourceLocation getModelLocation(T t) {
        return path;
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T t) {
        return animation;
    }
}
