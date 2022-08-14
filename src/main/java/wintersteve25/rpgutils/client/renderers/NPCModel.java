package wintersteve25.rpgutils.client.renderers;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class NPCModel extends AnimatedGeoModel<NPCEntity> {

    public NPCModel() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(NPCEntity entity) {
        return new ResourceLocation(RPGUtils.MOD_ID, "geo/npc.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity entity) {
        return new ResourceLocation(RPGUtils.MOD_ID, "textures/entity/npc/npc.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(NPCEntity entity) {
        return new ResourceLocation(RPGUtils.MOD_ID, "animations/npc.animation.json");
    }
}
