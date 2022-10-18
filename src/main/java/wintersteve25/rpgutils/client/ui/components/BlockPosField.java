package wintersteve25.rpgutils.client.ui.components;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;

public class BlockPosField extends Panel {
    
    private final TextBox x;
    private final TextBox y;
    private final TextBox z;
    
    public BlockPosField(Panel panel) {
        super(panel);
        
        x = newTextBox("X");
        y = newTextBox("Y");
        z = newTextBox("Z");
    }

    @Override
    public void addWidgets() {
        int w = width / 3 - 2;
        
        x.setSize(w, height);
        y.setSize(w, height);
        z.setSize(w, height);
        
        add(x);
        add(y);
        add(z);
    }

    @Override
    public void alignWidgets() {
        align(new WidgetLayout.Horizontal(0, 2, 0));
    }
    
    public BlockPos get() {
        String xText = x.getText();
        String yText = y.getText();
        String zText = z.getText();
        
        if (xText.isEmpty() && yText.isEmpty() && zText.isEmpty()) {
            return null;
        }
        
        if (xText.isEmpty()) {
            xText = "0";
        }
        
        if (yText.isEmpty()) {
            yText = "0";
        }

        if (zText.isEmpty()) {
            zText = "0";
        }
        
        return new BlockPos(
                Integer.parseInt(xText),
                Integer.parseInt(yText),
                Integer.parseInt(zText)
        );
    }
    
    private TextBox newTextBox(String ghostText) {
        TextBox box = new TextBox(this) {
            @Override
            public boolean isValid(String txt) {
                return NumberUtils.isCreatable(txt);
            }
        };
        box.ghostText = TextFormatting.WHITE + ghostText;
        
        return box;
    }
}
