package wintersteve25.rpgutils.common.data.loaded.quest.rewards;

import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.HashMap;
import java.util.Map;

public class RewardTypes {
    public static final Map<String, IDeserializer<IReward>> DESERIALIZERS;

    static {
        DESERIALIZERS = new HashMap<>();
        DESERIALIZERS.put("item", new ItemReward.Deserializer());
    }
}
