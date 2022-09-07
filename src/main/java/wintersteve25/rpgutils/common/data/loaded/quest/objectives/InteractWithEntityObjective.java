package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.String;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.IOpenableScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates.EntityPredicate;
import wintersteve25.rpgutils.common.data.loaded.quest.objectives.triggers.InteractEntityTrigger;
import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.function.Consumer;

public class InteractWithEntityObjective extends TriggeredObjective<InteractEntityTrigger> {
    
    private final EntityPredicate predicate;
    
    public InteractWithEntityObjective(EntityPredicate entityPredicate) {
        super(InteractEntityTrigger.class, trigger -> entityPredicate.matches(trigger.getPlayer(), trigger.getInteracted()));
        predicate = entityPredicate;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        
        object.addProperty("type", "interactEntity");
        object.add("predicate", predicate.serializeToJson());
        
        return object;
    }

    @Override
    public Icon objectiveIcon() {
        return Icons.PLAYER_GRAY;
    }

    @Override
    public ITextComponent objectiveTitle() {
        return new StringTextComponent("Interact with entity");
    }

    public static class Type implements IObjectiveType<InteractWithEntityObjective> {
        @Override
        public InteractWithEntityObjective fromJson(JsonObject jsonObject) {
            return new InteractWithEntityObjective(EntityPredicate.fromJson(jsonObject.get("predicate")));
        }

        @Override
        public IOpenableScreen configScreen(Consumer<InteractWithEntityObjective> onSubmit) {
            return null;
        }

        @Override
        public ITextComponent name() {
            return new StringTextComponent("Interact Entity");
        }
    }
}