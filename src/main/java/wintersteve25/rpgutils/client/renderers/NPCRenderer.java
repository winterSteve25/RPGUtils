package wintersteve25.rpgutils.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class NPCRenderer extends GeoEntityRenderer<NPCEntity> {

    public NPCRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NPCModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity instance) {
        return new ResourceLocation(RPGUtils.MOD_ID, "textures/entity/npc/npc.png");
    }
}
