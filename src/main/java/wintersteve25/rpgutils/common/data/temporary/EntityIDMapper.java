package wintersteve25.rpgutils.common.data.temporary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityIDMapper {
    public static final EntityIDMapper CLIENT = new EntityIDMapper();
    
    public final Map<UUID, Integer> data = new HashMap<>();
    
    public int getEntityId(UUID entityUUID) {
        return data.get(entityUUID);
    }
}