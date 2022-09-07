package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.text.StringTextComponent;

public class SubmitOrCancel extends Panel {
    private final Button submitButton;
    private final Button cancelButton;

    public SubmitOrCancel(Panel parent, Runnable onSubmit, Runnable onCancel) {
        super(parent);

        submitButton = new SimpleTextButton(this, new StringTextComponent("Confirm"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    onSubmit.run();
                }
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };

        cancelButton = new SimpleTextButton(this, new StringTextComponent("Cancel"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if (mouseButton.isLeft()) {
                    playClickSound();
                    onCancel.run();
                }
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
    }

    @Override
    public void addWidgets() {
        int w = width / 2 - 3;
        submitButton.setSize(w, height);
        cancelButton.setSize(w, height);
        add(submitButton);
        add(cancelButton);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Horizontal(0, 6, 0));
    }
}
