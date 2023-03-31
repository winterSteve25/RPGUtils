package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.LabeledWidget;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.BlockPredicate;

import java.util.function.Consumer;

public class BlockPredicateEditor extends BaseScreen {

    private final BlockPredicate.Builder builder;
    
    private final LabeledWidget<SimpleTextButton> blockInput;
    private final LabeledWidget<SimpleTextButton> tagInput;
    private final SubmitOrCancel submitOrCancel;
    
    public BlockPredicateEditor(Consumer<BlockPredicate> onSubmit, Runnable onCancel) {
        this(null, onSubmit, onCancel);
    }
    
    public BlockPredicateEditor(BlockPredicate initialPredicate, Consumer<BlockPredicate> onSubmit, Runnable onCancel) {
        this.builder = initialPredicate == null ? BlockPredicate.Builder.block() : BlockPredicate.Builder.fromPredicate(initialPredicate);
        
        blockInput = new LabeledWidget<>(this, p -> new SimpleTextButton(p, new StringTextComponent("None"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                ItemStackConfig config = new ItemStackConfig(true, false);
                SelectBlockScreen.open(config, selected -> {
                    BlockPredicateEditor.this.openGui();

                    if (!selected) {
                        return;
                    }

                    Item item = config.value.getItem();
                    
                    if (item instanceof BlockItem) {
                        Block block = ((BlockItem) item).getBlock();
                        builder.of(block);
                        BlockPredicateEditor.this.blockInput.refreshWidgets();
                        BlockPredicateEditor.this.alignWidgets();
                    }
                });
            }
        }, new StringTextComponent("Block: "));
        blockInput.setSize(140, 20);
        
        tagInput = new LabeledWidget<>(this, p -> new SimpleTextButton(p, new StringTextComponent("None"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                SelectTag.open(false, BlockTags.getAllTags(), selected -> {
                    
                    if (selected.isEmpty()) {
                        BlockPredicateEditor.this.openGui();
                        return;
                    }
                    
                    SelectTag.SelectTagOption<Block> tag = selected.get(0);
                    builder.of(tag.getTag());
                    BlockPredicateEditor.this.openGui();
                    BlockPredicateEditor.this.alignWidgets();
                });
            }
        }, new StringTextComponent("Block Tag: "));
        tagInput.setSize(140, 20);
        
        submitOrCancel = new SubmitOrCancel(this, () -> {
            onSubmit.accept(builder.build());
        }, onCancel);
        submitOrCancel.setSize(140, 20);
    }
    
    @Override
    public void addWidgets() {
        TextField textField = new TextField(this);
        textField.setText("Block Predicate");
        add(textField);
        add(blockInput);
        add(tagInput);
        add(submitOrCancel);
    }

    @Override
    public void alignWidgets() {
        tagInput.alignWidgets();
        align(new CenterLayout(10));

        if (builder.getBlock() != null) {
            blockInput.getWidget().setTitle(new StringTextComponent(builder.getBlock().getRegistryName().toString()));
            blockInput.getWidget().setIcon(ItemIcon.getItemIcon(builder.getBlock().asItem()));
        }
        
        if (builder.getBlocks() != null) {
            tagInput.getWidget().setTitle(new StringTextComponent(TagCollectionManager.getInstance().getBlocks().getIdOrThrow(builder.getBlocks()).toString()));
        }
    }
}
