package wintersteve25.rpgutils.client.renderers.base;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class GeckolibBlockRendererBase<T extends TileEntity & IAnimatable> extends GeoBlockRenderer<T> {
    public GeckolibBlockRendererBase(TileEntityRendererDispatcher rendererDispatcherIn, AnimatedGeoModel<T> modelProvider) {
        super(rendererDispatcherIn, modelProvider);
    }
}
