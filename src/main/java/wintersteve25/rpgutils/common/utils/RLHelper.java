package wintersteve25.rpgutils.common.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.RPGUtils;

import java.nio.file.Path;

public class RLHelper {
    public static ResourceLocation geoModel(String name) {
        return new ResourceLocation(RPGUtils.MOD_ID, "geo/" + name + ".geo.json");
    }
    
    public static ResourceLocation geoAnim(String name) {
        return new ResourceLocation(RPGUtils.MOD_ID, "animations/" + name + ".animation.json");
    }
    
    public static ResourceLocation texture(String name) {
        return new ResourceLocation(RPGUtils.MOD_ID, "textures/" + name + ".png");
    }

    public static TranslationTextComponent dialogueEditorComponent(String path, Object... args) {
        return base("rpgutils.gui.dialogue_editor", path, args);
    }
    
    public static TranslationTextComponent dialogueCreatorComponent(String path, Object... args) {
        return base("rpgutils.gui.dialogue_creator", path, args);
    }
    
    public static TranslationTextComponent uiComponent(String path, Object... args) {
        return base("rpgutils.gui", path, args);
    }
    
    private static TranslationTextComponent base(String base, String path, Object... args) {
        return new TranslationTextComponent(base + "." + path, args);
    }
}