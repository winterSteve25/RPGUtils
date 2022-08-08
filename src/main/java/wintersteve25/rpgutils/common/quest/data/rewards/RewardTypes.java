package wintersteve25.rpgutils.common.quest.data.rewards;

import wintersteve25.rpgutils.common.utils.IDeserializer;

import java.util.HashMap;
import java.util.Map;

public class RewardTypes {
    public static final Map<String, IDeserializer<IReward>> SERIALIZERS;

    static {
        SERIALIZERS = new HashMap<>();
        SERIALIZERS.put("item", new ItemReward.Deserializer());
    }
}
