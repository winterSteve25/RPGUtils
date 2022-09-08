package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.components.CenterLayout;
import wintersteve25.rpgutils.client.ui.components.SubmitOrCancel;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.BlockPredicate;

import java.util.function.Consumer;

public class BlockPredicateEditor extends BaseScreen {
    
    private final Consumer<BlockPredicate> onSubmit;
    private final BlockPredicate.Builder builder;
    
    private final Button tagInput;
    private final SubmitOrCancel submitOrCancel;
    
    public BlockPredicateEditor(Consumer<BlockPredicate> onSubmit) {
        this.onSubmit = onSubmit;
        this.builder = BlockPredicate.Builder.block();
        
        tagInput = new SimpleTextButton(this, new StringTextComponent("Block Tag"), Icon.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                Minecraft.getInstance().setScreen(new SelectTag<>(false, BlockTags.getAllTags(), selected -> {
                    builder.of(selected.get(0).getTag());
                    BlockPredicateEditor.this.openGui();
                }));
            }
        };
        tagInput.setSize(160, 20);
        
        submitOrCancel = new SubmitOrCancel(this, () -> {
            
        }, () -> {
            
        });
        submitOrCancel.setSize(160, 20);
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
        align(new CenterLayout(10));
    }
}
