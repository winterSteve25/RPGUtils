package wintersteve25.rpgutils.client.renderers;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class NPCModel extends AnimatedGeoModel<NPCEntity> {
    public NPCModel() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(NPCEntity npcEntity) {
        return npcEntity.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity npcEntity) {
        return npcEntity.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(NPCEntity npcEntity) {
        return npcEntity.getAnimationLocation();
    }
}
