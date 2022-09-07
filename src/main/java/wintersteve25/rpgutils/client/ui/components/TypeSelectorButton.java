package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.BiConsumer;

public class TypeSelectorButton {
    public static final TypeSelectorButton DEFAULT = new TypeSelectorButton();
    
    private Icon icon = Icon.EMPTY;
    private ITextComponent title = StringTextComponent.EMPTY;
    private BiConsumer<Button, MouseButton> onClicked = (btn, mb) -> {};

    public TypeSelectorButton(Icon icon, ITextComponent title, BiConsumer<Button, MouseButton> onClicked) {
        this.icon = icon;
        this.title = title;
        this.onClicked = onClicked;
    }
    
    public TypeSelectorButton() {
    }

    public Icon getIcon() {
        return icon;
    }

    public ITextComponent getTitle() {
        return title;
    }

    public BiConsumer<Button, MouseButton> getOnClicked() {
        return onClicked;
    }
}
