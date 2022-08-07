package wintersteve25.rpgutils.client.ui.select_entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectEntity extends BaseUI {
    public static final ResourceLocation BG = new ResourceLocation(RPGUtils.MOD_ID, "textures/gui/select_entity.png");
    private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    
    private static final int ITEMS_EACH_PAGE = 8;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 171;
    private static final TranslationTextComponent SEARCH_HINT = new TranslationTextComponent("rpgutils.gui.select_entity.search");
    private static final TranslationTextComponent CONFIRM_TEXT = new TranslationTextComponent("rpgutils.gui.select_entity.confirm");
    
    private final boolean allowMultiple;
    private final List<Integer> selectedIndices;
    private final Consumer<List<EntityOption>> onSubmit; 
    
    private List<EntityOption> totalOptions;
    private List<EntityOption> filteredOptions;
    private TextFieldWidget searchBar;

    private ToggleWidget forwardButton;
    private ToggleWidget backButton;
    private Button confirm;
    
    private int totalPages;
    private int page;
    
    public SelectEntity(boolean allowMultiple, Consumer<List<EntityOption>> onSubmit) {
        super(BG, WIDTH, HEIGHT);
        
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
            onSubmit.accept(selectedIndices.stream().map(index -> totalOptions.get(index)).collect(Collectors.toList()));
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

        if (!searchBar.isFocused() && searchBar.getValue().isEmpty()) {
            drawString(matrixStack, minecraft.font, SEARCH_HINT, this.x + 14, this.y + 16, TextFormatting.GRAY.getColor());
        }
    }

    @Override
    public void tick() {
        searchBar.tick();
        
        for (EntityOption option : filteredOptions) {
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

        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos posStart = player.blockPosition().offset(-16, -16, -16);
        BlockPos posEnd = player.blockPosition().offset(16, 16, 16);

        List<Entity> entitiesInRange = player.getCommandSenderWorld().getEntities(player, new AxisAlignedBB(posStart, posEnd));
        for (int i = 0; i < entitiesInRange.size(); i++) {
            totalOptions.add(new EntityOption(this.x + 10, this.y + 40 + i * 12, entitiesInRange.get(i), this, i));
        }
        
        if (!initializeFilteredOptions) return; 
        this.filteredOptions = new ArrayList<>(totalOptions);
        this.totalPages = filteredOptions.size() / 10;
    }
    
    private void updateFilteredOptions() {
        List<EntityOption> totalCopy = new ArrayList<>(totalOptions);
        
        if (!searchBar.getValue().isEmpty()) {
            totalCopy.removeIf(element -> !element.getText().toLowerCase().contains(searchBar.getValue().toLowerCase()));
        }

        this.buttons.removeIf(button -> button instanceof EntityOption);
        this.children.removeIf(button -> button instanceof EntityOption);
        
        filteredOptions.clear();
        int y = this.y + 40;

        for (int i = ITEMS_EACH_PAGE * page; i < ITEMS_EACH_PAGE * (page + 1); i++) {
            if (i >= totalCopy.size()) break;
            EntityOption copy = new EntityOption(totalCopy.get(i));
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
    
    private void updateConfirmButton() {
        confirm.active = !selectedIndices.isEmpty();
    }
}