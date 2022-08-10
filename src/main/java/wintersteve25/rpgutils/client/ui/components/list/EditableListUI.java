package wintersteve25.rpgutils.client.ui.components.list;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EditableListUI<T extends AbstractListEntryWidget> extends BaseUI {

    private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    private static final TranslationTextComponent SAVE = RLHelper.dialogueEditorComponent("save");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 240;
    public static final int ITEMS_EACH_PAGE = 7;

    protected final TranslationTextComponent title;
    protected final List<T> listEntries;

    private ToggleWidget forwardButton;
    private ToggleWidget backButton;
    private int totalPages;
    private int page;
    
    protected EditableListUI(TranslationTextComponent title, int texWidth, int texHeight) {
        super(null, texWidth, texHeight);
        this.title = title;
        this.listEntries = new ArrayList<>();
    }

    protected EditableListUI(TranslationTextComponent title) {
        this(title, WIDTH, HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        this.y -= 20;
        int rightX = this.x + texWidth;

        Button addNewButton = new Button(rightX - 95, this.y + 5, 20, 20, new StringTextComponent("+"), btn -> {
            T entry = createEntry(listEntries.size());
            
            if (listEntries.size() - totalPages * ITEMS_EACH_PAGE < ITEMS_EACH_PAGE) {
                entry.init(this.x + 15, this.y, this);
            }
            
            listEntries.add(entry);
            totalPages = (int) Math.ceil(listEntries.size() / (float) ITEMS_EACH_PAGE);
            updatePageFlipButtons();
        });
        addButton(addNewButton);

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
        addButton(removeButton);

        Button saveButton = new Button(rightX - 45, this.y + 5, 40, 20, SAVE, btn -> save(listEntries));
        addButton(saveButton);

        this.forwardButton = new ToggleButton(this.x + 385, this.y + 245, 12, 17, false, btn -> {
            if (page >= totalPages) return;
            page++;
            updateActiveEntries();
            updatePageFlipButtons();
        });
        this.forwardButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK_LOCATION);
        this.backButton = new ToggleButton(this.x + 5, this.y + 245, 12, 17, true, btn -> {
            if (page <= 0) return;
            page--;
            updateActiveEntries();
            updatePageFlipButtons();
        });
        this.backButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK_LOCATION);
        addButton(forwardButton);
        addButton(backButton);
        
        for (T entry : listEntries) {
            entry.init(this.x + 15, this.y, this);
        }
        
        updateActiveEntries();
        updatePageFlipButtons();
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        drawString(pMatrixStack, minecraft.font, title, this.x + 15, this.y + 10, TextFormatting.WHITE.getColor());
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public void tick() {
        for (int i = ITEMS_EACH_PAGE * page; i < ITEMS_EACH_PAGE * (page + 1); i++) {
            if (i >= listEntries.size()) break;
            T entry = listEntries.get(i);
            entry.tick();
        }
    }
    
    private void updateActiveEntries() {
        for (T entry : listEntries) {
            entry.remove(this);
        }
        
        for (int i = ITEMS_EACH_PAGE * page; i < ITEMS_EACH_PAGE * (page + 1); i++) {
            if (i >= listEntries.size()) break;
            listEntries.get(i).init(this.x + 15, this.y, this);
        }
    }

    private void updatePageFlipButtons() {
        this.forwardButton.visible = this.totalPages > 1 && this.page < this.totalPages - 1;
        this.backButton.visible = this.totalPages > 1 && this.page > 0;
    }
    
    protected abstract T createEntry(int index);

    protected abstract void save(List<T> data);
}
