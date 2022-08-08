package wintersteve25.rpgutils.client.ui.components.list;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EditableListUI<T extends AbstractListEntryWidget> extends BaseUI {
    
    private static final TranslationTextComponent TITLE = RLHelper.dialogueEditorComponent("title");
    private static final TranslationTextComponent SAVE = RLHelper.dialogueEditorComponent("save");
    
    protected final List<T> listEntries;
    
    protected EditableListUI(ResourceLocation bg, int texWidth, int texHeight) {
        super(bg, texWidth, texHeight);
        this.listEntries = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();

        int rightX = this.x + texWidth;

        Button addNewButton = new Button(rightX - 95, this.y + 5, 20, 20, new StringTextComponent("+"), btn -> {
            T entry = createEntry(listEntries.size());
            entry.init(this.x + 15, this.y, this);
            listEntries.add(entry);
        });

        Button removeButton = new Button(rightX - 70, this.y + 5, 20, 20, new StringTextComponent("-"), btn -> {
            List<T> matches = listEntries.stream().filter(AbstractListEntryWidget::isSelected).collect(Collectors.toList());

            for (T widget : matches) {
                widget.remove(this);
                listEntries.remove(widget);
            }

            // update the remaining ones' index
            for (int i = 0; i < listEntries.size(); i++) {
                T actionGui = listEntries.get(i);
                // does not require update
                if (actionGui.getIndex() == i) continue;
                actionGui.setIndex(i);
                actionGui.init(this.x + 15, this.y, this);
            }
        });

        Button saveButton = new Button(rightX - 45, this.y + 5, 40, 20, SAVE, btn -> {
            save(listEntries);
        });

        for (T entry : listEntries) {
            entry.init(this.x + 15, this.y, this);
        }

        addButton(addNewButton);
        addButton(removeButton);
        addButton(saveButton);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);

        pMatrixStack.pushPose();

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(bg);

        blit(pMatrixStack, this.x, this.y, getBlitOffset(), 0, 0, texWidth, texHeight, 512, 512);
        drawString(pMatrixStack, minecraft.font, TITLE, this.x + 15, this.y + 10, TextFormatting.WHITE.getColor());

        pMatrixStack.popPose();

        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public void tick() {
        for (T entry : listEntries) {
            entry.tick();
        }
    }
    
    protected abstract T createEntry(int index);

    protected abstract void save(List<T> data);
}
