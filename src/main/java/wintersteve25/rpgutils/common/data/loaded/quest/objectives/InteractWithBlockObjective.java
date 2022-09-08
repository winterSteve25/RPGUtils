package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.IOpenableScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.client.ui.quests.creator.BlockPredicateEditor;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.BlockPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractBlockTrigger;

import java.util.function.Consumer;

public class InteractWithBlockObjective extends TriggeredObjective<InteractBlockTrigger> {
    
    private final BlockPredicate predicate;
    
    public InteractWithBlockObjective(BlockPredicate blockPredicate) {
        super(InteractBlockTrigger.class, trigger -> blockPredicate.matches(trigger.getWorld(), trigger.getPos()));
        predicate = blockPredicate;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("type", "interactBlock");
        jsonObject.add("predicate", predicate.serializeToJson());
        
        return jsonObject;
    }

    @Override
    public Icon objectiveIcon() {
        return Icons.BEACON;
    }

    @Override
    public ITextComponent objectiveTitle() {
        return new StringTextComponent("Interact with block");
    }

    public static class Type implements IObjectiveType<InteractWithBlockObjective> {
        @Override
        public InteractWithBlockObjective fromJson(JsonObject jsonObject) {
            return new InteractWithBlockObjective(BlockPredicate.fromJson(jsonObject.get("predicate")));
        }

        @Override
        public void openConfigScreen(Consumer<InteractWithBlockObjective> onSubmit, Runnable onCancel) {
            new BlockPredicateEditor(predicate -> onSubmit.accept(new InteractWithBlockObjective(predicate)), onCancel)
                    .openGui();
        }

        @Override
        public ITextComponent name() {
            return new StringTextComponent("Interact Block");
        }
    }
}
