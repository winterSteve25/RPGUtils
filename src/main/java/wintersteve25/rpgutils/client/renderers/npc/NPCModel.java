package wintersteve25.rpgutils.client.renderers.npc;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.npc.property.StringNPCProperty;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.entities.NPCType;

public class NPCModel extends AnimatedGeoModel<NPCEntity> {

    private static final ResourceLocation DEFAULT_PATH = new ResourceLocation(RPGUtils.MOD_ID, "geo/npc.geo.json");
    private static final ResourceLocation DEFAULT_ANIMATIONS = new ResourceLocation(RPGUtils.MOD_ID, "animations/npc.animation.json");

    @Override
    public ResourceLocation getModelLocation(NPCEntity entity) {
        NPCType type = entity.getNPCType();
        if (type != null) {
            return new ResourceLocation(RPGUtils.MOD_ID, (String) type.getProperty(StringNPCProperty.MODEL));
        }
        return DEFAULT_PATH;
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity entity) {
        return new ResourceLocation(RPGUtils.MOD_ID, entity.getTexturePath());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(NPCEntity entity) {
        NPCType type = entity.getNPCType();
        if (type != null) {
            return new ResourceLocation(RPGUtils.MOD_ID, (String) type.getProperty(StringNPCProperty.ANIMATIONS));
        }
        return DEFAULT_ANIMATIONS;
    }
}
