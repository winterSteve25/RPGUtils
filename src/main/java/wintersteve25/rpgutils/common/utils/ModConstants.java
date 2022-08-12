package wintersteve25.rpgutils.common.utils;

import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;

import java.util.UUID;

public class ModConstants {
    public static final UUID INVALID_UUID = new UUID(0, 0);
    
    public static class Resources {
        public static final ResourceLocation BLANK_SCREEN = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/blank_screen.png");
    }
}