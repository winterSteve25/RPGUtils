package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;

import java.util.function.BiConsumer;

public class TextInputPrompt extends BaseScreen {

    private boolean enabled;
    
    public final TextField title;
    public final TextBox enterText;
    public final Panel buttons;
    
    public TextInputPrompt(Panel panel, ITextComponent title, String hintText, BiConsumer<TextInputPrompt, MouseButton> onConfirmed, BiConsumer<TextInputPrompt, MouseButton> onDenied) {
        this.parent = panel;
        this.title = new TextField(this);
        this.title.setSize(100, 20);
        this.title.setText(title);
        enterText = new TextBox(this);
        enterText.setSize(100, 20);
        enterText.ghostText = hintText;
        
        buttons = new Panel(this) {
            @Override
            public void addWidgets() {
                Button confirm = new SimpleTextButton(this, new StringTextComponent("Confirm"), Icon.EMPTY) {
                    @Override
                    public void onClicked(MouseButton mouseButton) {
                        onConfirmed.accept(TextInputPrompt.this, mouseButton);
                    }

                    @Override
                    public boolean renderTitleInCenter() {
                        return true;
                    }
                };
                confirm.setSize(45, 20);
                add(confirm);

                Button cancel = new SimpleTextButton(this, new StringTextComponent("Cancel"), Icon.EMPTY) {
                    @Override
                    public void onClicked(MouseButton mouseButton) {
                        onDenied.accept(TextInputPrompt.this, mouseButton);
                    }

                    @Override
                    public boolean renderTitleInCenter() {
                        return true;
                    }
                };
                cancel.setSize(45, 20);
                add(cancel);
            }

            @Override
            public void alignWidgets() {
                align(new WidgetLayout.Horizontal(0, 10, 0));
            }
        };
    }

    @Override
    public void addWidgets() {
        if (!enabled) return;
        add(title);
        add(enterText);
        add(buttons);
        buttons.setSize(100, 20);
    }

    @Override
    public void alignWidgets() {
        align(new CenterLayout(10));
    }

    @Override
    public boolean shouldDraw() {
        return enabled;
    }
    
    public void disable() {
        enabled = false;
        initGui();
    }
    
    public void enable() {
        enabled = true;
        initGui();
    }
}
