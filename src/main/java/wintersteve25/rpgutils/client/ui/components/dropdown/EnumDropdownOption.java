package wintersteve25.rpgutils.client.ui.components.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumDropdownOption<T extends Enum<T>> implements IDropdownOption {
    
    private final T value;

    public EnumDropdownOption(T value) {
        this.value = value;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY) {
        AbstractGui.drawCenteredString(matrixStack, Minecraft.getInstance().font, value.toString(), x, y, TextFormatting.WHITE.getColor());
    }

    public T getValue() {
        return value;
    }

    public static <T extends Enum<T>> List<EnumDropdownOption<T>> populate(Class<T> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).map(EnumDropdownOption::new).collect(Collectors.toList());
    }
}
