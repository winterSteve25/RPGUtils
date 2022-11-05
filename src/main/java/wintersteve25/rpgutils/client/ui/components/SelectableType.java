package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.BiConsumer;

public class SelectableType<T> {
    
    private final Icon icon;
    private final ITextComponent title;
    private final BiConsumer<Button, MouseButton> onClicked;
    private final T type;
    
    public SelectableType(Icon icon, ITextComponent title, BiConsumer<Button, MouseButton> onClicked, T type) {
        this.icon = icon;
        this.title = title;
        this.onClicked = onClicked;
        this.type = type;
    }
    
    public SelectableType(T type) {
        this(Icon.EMPTY, StringTextComponent.EMPTY, (b, m) -> {}, type);
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

    public T getType() {
        return type;
    }
}