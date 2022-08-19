package wintersteve25.rpgutils.client.renderers.npc;

import wintersteve25.rpgutils.client.renderers.GeckolibModelBase;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class NPCModel extends GeckolibModelBase<NPCEntity> {

    public NPCModel() {
        super("geo/npc.geo.json", "textures/entity/npc/npc.png", "animations/npc.animation.json");
    }
}
