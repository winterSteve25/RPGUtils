package wintersteve25.rpgutils.common.utils;

import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;

public class ModConstants {
    public static class PacketTypes {
        public static final byte OPEN_GUI = 0;
        public static final byte SET_DATA = 1;
    }
    
    public static class Resources {
        public static final ResourceLocation BLANK_SCREEN = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/blank_screen.png");
    }
}