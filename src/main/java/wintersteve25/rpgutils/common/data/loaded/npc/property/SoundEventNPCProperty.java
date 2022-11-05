package wintersteve25.rpgutils.common.data.loaded.npc.property;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;

public class SoundEventNPCProperty extends NPCProperty<SoundEvent> {

    public static final SoundEventNPCProperty AMBIENT_SOUND = new SoundEventNPCProperty("ambientSound", "sounds", SoundEvents.VILLAGER_AMBIENT);
    public static final SoundEventNPCProperty HURT_SOUND = new SoundEventNPCProperty("hurtSound", "sounds", SoundEvents.VILLAGER_HURT);
    public static final SoundEventNPCProperty DEATH_SOUND = new SoundEventNPCProperty("deathSound", "sounds", SoundEvents.VILLAGER_DEATH);

    protected SoundEventNPCProperty(String jsonName, String group, SoundEvent defaultValue) {
        super(jsonName, group, defaultValue);
    }

    @Override
    public SoundEvent deserialiseJson(JsonElement json) {
        return NPCTypeLoader.SOUND_EVENTS.get(json.getAsString());
    }

    @Override
    public SoundEvent deserialisePacket(PacketBuffer buffer) {
        return NPCTypeLoader.SOUND_EVENTS.get(buffer.readUtf());
    }

    @Override
    public void serialisePacket(PacketBuffer buffer, SoundEvent value) {
        buffer.writeUtf(NPCTypeLoader.SOUND_EVENTS.get(value));
    }
}
