package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.function.BiPredicate;

public class LocalPlayerHealthPredicate extends DialoguePredicate {

    private final HealthPredicateType type;
    private final float operand;

    public LocalPlayerHealthPredicate(HealthPredicateType type, float operand) {
        super(null);
        this.type = type;
        this.operand = operand;
    }

    protected LocalPlayerHealthPredicate(JsonObject jsonObject) {
        super(jsonObject);
        String typeStr = jsonObject.get("type").getAsString();
        this.type = HealthPredicateType.valueOf(typeStr);
        this.operand = jsonObject.get("operand").getAsFloat();
    }

    @Override
    public boolean test() {
        float health = Minecraft.getInstance().player.getHealth();
        return type.test(health, operand);
    }

    public enum HealthPredicateType implements BiPredicate<Float, Float> {
        LESS_THAN {
            @Override
            public boolean test(Float health, Float operand) {
                return health < operand;
            }
        },
        EQUAL_TO {
            @Override
            public boolean test(Float health, Float operand) {
                return health.equals(operand);
            }
        },
        GREATER_THAN {
            @Override
            public boolean test(Float health, Float operand) {
                return health > operand;
            }
        }
    }
}
