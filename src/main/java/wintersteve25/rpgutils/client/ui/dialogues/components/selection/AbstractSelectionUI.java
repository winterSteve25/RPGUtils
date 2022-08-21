package wintersteve25.rpgutils.client.ui.dialogues.components.selection;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.dialogues.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.dialogues.components.BaseUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.buttons.ToggleButton;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractSelectionUI<T extends SelectionOption<T>> extends BaseUI {
    private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    
    private static final int ITEMS_EACH_PAGE = 8;
    public static final int WIDTH = 176;
    public static final int HEIGHT = 171;
    private static final TranslationTextComponent SEARCH_HINT = RLHelper.uiComponent("select_entity.search");
    public static final TranslationTextComponent CONFIRM_TEXT = RLHelper.uiComponent("select_entity.confirm");
    
    private final boolean allowMultiple;
    private final List<Integer> selectedIndices;
    private final Consumer<List<T>> onSubmit; 

    private List<T> totalOptions;
    private List<T> filteredOptions;
    private TextFieldWidget searchBar;

    private ToggleWidget forwardButton;
    private ToggleWidget backButton;
    private Button confirm;
    
    private int totalPages;
    private int page;
    
    public AbstractSelectionUI(boolean allowMultiple, Consumer<List<T>> onSubmit) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, HEIGHT);
        
        this.onSubmit = onSubmit;
        this.selectedIndices = new ArrayList<>();
        this.page = 0;
        this.allowMultiple = allowMultiple;
        
        MainWindow window = Minecraft.getInstance().getWindow();
        this.x = (window.getGuiScaledWidth() - WIDTH) / 2;
        this.y = (window.getGuiScaledHeight() - HEIGHT) / 2;
        updateTotalOptions(true);
    }

    @Override
    protected void init() {
        super.init();
        
        String value = searchBar == null ? "" : searchBar.getValue();
        searchBar = new TextFieldWidget(Minecraft.getInstance().font, this.x + 10, this.y + 10, 155, 20, SEARCH_HINT);
        searchBar.setMaxLength(50);
        searchBar.setVisible(true);
        searchBar.setTextColor(16777215);
        searchBar.setValue(value);
        searchBar.setResponder(str -> updateFilteredOptions());
        addButton(searchBar);

        this.forwardButton = new ToggleButton(this.x + 155, this.y + 145, 12, 17, false, btn -> {
            page++;
            updateFilteredOptions();
        });
        this.forwardButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK_LOCATION);
        this.backButton = new ToggleButton(this.x + 10, this.y + 145, 12, 17, true, btn -> {
            page--;
            updateFilteredOptions();
        });
        this.backButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK_LOCATION);
        addButton(forwardButton);
        addButton(backButton);
        
        //int pX, int pY, int pWidth, int pHeight, ITextComponent pMessage, Button.IPressable pOnPress
        confirm = new Button(this.x + (WIDTH - 70) / 2, this.y + 145, 70, 20, CONFIRM_TEXT, btn -> {
            onSubmit.accept(getSelected());
        });
        addButton(confirm);

        updateTotalOptions(false);
        updateFilteredOptions();
        updateConfirmButton();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        // background
        renderBackgroundTexture(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        UIUtilities.textFieldHint(matrixStack, SEARCH_HINT, searchBar);
    }

    @Override
    public void tick() {
        searchBar.tick();
        
        for (T option : filteredOptions) {
            option.setSelected(selectedIndices.contains(option.getIndex()));
        }
    }
    
    public void setSelectedIndex(int i) {
        if (allowMultiple) {
            if (selectedIndices.contains(i)) {
                selectedIndices.remove((Object) i);
            } else {
                selectedIndices.add(i);
            }
            updateConfirmButton();
            return;
        }
        
        selectedIndices.clear();
        selectedIndices.add(i);
        updateConfirmButton();
    }
    
    private void updatePageFlipButtons() {
        this.forwardButton.visible = this.totalPages > 1 && this.page < this.totalPages - 1;
        this.backButton.visible = this.totalPages > 1 && this.page > 0;
    }
    
    private void updateTotalOptions(boolean initializeFilteredOptions) {
        this.totalOptions = new ArrayList<>();
        populateOptions(totalOptions);
        if (!initializeFilteredOptions) return; 
        this.filteredOptions = new ArrayList<>(totalOptions);
        this.totalPages = filteredOptions.size() / ITEMS_EACH_PAGE;
    }

    private void updateFilteredOptions() {
        List<T> totalCopy = new ArrayList<>(totalOptions);
        
        if (!searchBar.getValue().isEmpty()) {
            totalCopy.removeIf(element -> filterSearch(element, searchBar.getValue().toLowerCase()));
        }

        this.buttons.removeIf(this::isSameType);
        this.children.removeIf(this::isSameType);
        
        filteredOptions.clear();
        int y = this.y + 40;

        for (int i = ITEMS_EACH_PAGE * page; i < ITEMS_EACH_PAGE * (page + 1); i++) {
            if (i >= totalCopy.size()) break;
            T copy = copyFrom(totalCopy.get(i));
            copy.y = y;
            y+=12;
            addButton(copy);
            filteredOptions.add(copy);
        }
        
        this.totalPages = (int) Math.ceil(totalCopy.size() / (float) ITEMS_EACH_PAGE);
        
        if (page > totalPages) {
            page = totalPages;
        }
        
        updatePageFlipButtons();
    }

    public List<T> getSelected() {
        return selectedIndices.stream().map(index -> totalOptions.get(index)).collect(Collectors.toList());
    }
    
    protected void updateConfirmButton() {
        confirm.active = !selectedIndices.isEmpty() && !totalOptions.isEmpty();
    }
    
    protected abstract void populateOptions(List<T> list);
    
    protected abstract T copyFrom(T copyFrom);
    
    protected abstract boolean isSameType(IGuiEventListener listener);
    
    protected boolean filterSearch(T element, String searchbarValue) {
        return !element.getText().toLowerCase().contains(searchbarValue);
    }
}