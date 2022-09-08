package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.LabeledWidget;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.BlockPredicate;

import java.util.function.Consumer;

public class BlockPredicateEditor extends BaseScreen {
    
    private final Consumer<BlockPredicate> onSubmit;
    private final BlockPredicate.Builder builder;
    
    private final LabeledWidget<SimpleTextButton> tagInput;
    private final SubmitOrCancel submitOrCancel;
    
    public BlockPredicateEditor(Consumer<BlockPredicate> onSubmit, Runnable onCancel) {
        this.onSubmit = onSubmit;
        this.builder = BlockPredicate.Builder.block();
        
        tagInput = new LabeledWidget<>(this, p -> new SimpleTextButton(p, new StringTextComponent("None"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                SelectTag.open(false, BlockTags.getAllTags(), selected -> {
                    SelectTag.SelectTagOption<Block> tag = selected.get(0);
                    builder.of(tag.getTag());
                    BlockPredicateEditor.this.openGui();
                    BlockPredicateEditor.this.tagInput.getWidget().setTitle(new StringTextComponent(tag.getText()));
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
        add(tagInput);        
        add(submitOrCancel);
    }

    @Override
    public void alignWidgets() {
        tagInput.alignWidgets();
        align(new CenterLayout(10));
    }
}
