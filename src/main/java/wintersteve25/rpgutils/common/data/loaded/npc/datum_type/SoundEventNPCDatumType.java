package wintersteve25.rpgutils.common.data.loaded.npc.datum_type;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;

public class SoundEventNPCDatumType extends NPCDatumType<SoundEvent> {

    public static final SoundEventNPCDatumType AMBIENT_SOUND = new SoundEventNPCDatumType("ambientSound");
    public static final SoundEventNPCDatumType HURT_SOUND = new SoundEventNPCDatumType("hurtSound");
    public static final SoundEventNPCDatumType DEATH_SOUND = new SoundEventNPCDatumType("deathSound");

    protected SoundEventNPCDatumType(String jsonName) {
        super(jsonName);
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

    public static void register() {
        register(AMBIENT_SOUND);
        register(HURT_SOUND);
        register(DEATH_SOUND);
    }
}
